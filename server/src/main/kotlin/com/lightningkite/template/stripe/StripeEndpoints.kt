package com.lightningkite.template.stripe

import com.lightningkite.lightningdb.*
import com.lightningkite.lightningserver.HtmlDefaults
import com.lightningkite.lightningserver.auth.user
import com.lightningkite.lightningserver.core.ServerPath
import com.lightningkite.lightningserver.core.ServerPathGroup
import com.lightningkite.lightningserver.exceptions.NotFoundException
import com.lightningkite.lightningserver.http.*
import com.lightningkite.lightningserver.routes.fullUrl
import com.lightningkite.lightningserver.schedule.schedule
import com.lightningkite.lightningserver.settings.generalSettings
import com.lightningkite.lightningserver.tasks.task
import com.lightningkite.template.*
import com.stripe.exception.SignatureVerificationException
import com.stripe.exception.StripeException
import com.stripe.model.Invoice
import com.stripe.model.Price
import com.stripe.model.Product
import com.stripe.model.Subscription
import com.stripe.model.WebhookEndpoint
import com.stripe.model.checkout.Session
import com.stripe.net.Webhook
import com.stripe.param.PriceCreateParams
import com.stripe.param.PriceListParams
import com.stripe.param.ProductCreateParams
import com.stripe.param.WebhookEndpointCreateParams
import com.stripe.param.checkout.SessionCreateParams
import kotlinx.coroutines.*
import java.time.Duration
import java.util.*

@OptIn(DelicateCoroutinesApi::class)
class StripeEndpoints(path: ServerPath) : ServerPathGroup(path) {
    val price = 5_99L
    val productId by lazy { "${generalSettings().projectName.filter { it.isLetterOrDigit() }}Monthly" }
    val subscriptionProduct = GlobalScope.async(start = CoroutineStart.LAZY) {
        try {
            Product.retrieve(productId)
        } catch (e: StripeException) {
            null
        }
            ?: Product.create(
                ProductCreateParams.builder()
                    .setId(productId)
                    .setName(generalSettings().projectName + " Monthly")
                    .setType(ProductCreateParams.Type.SERVICE)
                    .build()
            )
    }
    val subscriptionPricing = GlobalScope.async(start = CoroutineStart.LAZY) {
        try {
            Price.list(
                PriceListParams.builder()
                    .setProduct(productId)
                    .build()
            )
                .data
                .firstOrNull()
        } catch (e: StripeException) {
            null
        }
            ?: Price.create(
                PriceCreateParams.builder()
                    .setCurrency("usd")
                    .setUnitAmount(price)
                    .setProduct(subscriptionProduct.await().id)
                    .setRecurring(
                        PriceCreateParams.Recurring.builder()
                            .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                            .setTrialPeriodDays(30)
                            .build()
                    )
                    .build()
            )
    }
    val webhookConfig: Deferred<StripeWebhookSecret> = GlobalScope.async(start = CoroutineStart.LAZY) {
        val url = generalSettings().publicUrl + webhook.path.toString()
        (Server.database().collection<StripeWebhookSecret>()
            .get(path.toString()))
            ?: WebhookEndpoint.create(
                WebhookEndpointCreateParams.builder()
                    .setUrl(url)
                    .addEnabledEvent(WebhookEndpointCreateParams.EnabledEvent.CHECKOUT__SESSION__COMPLETED)
                    .addEnabledEvent(WebhookEndpointCreateParams.EnabledEvent.INVOICE__PAYMENT_FAILED)
                    .setApiVersion(WebhookEndpointCreateParams.ApiVersion.VERSION_2022_08_01)
                    .build()
            ).let {
                Server.database().collection<StripeWebhookSecret>().insertOne(
                    StripeWebhookSecret(
                        _id = path.toString(),
                        secret = it.secret
                    )
                )!!
            }
    }
    val landingSuccess = path("landing-success").get.handler {
        HttpResponse.Companion.html(content = HtmlDefaults.basePage("""
            <h2>Success!</h2>
            <p>You may now close this window.</p>
        """.trimIndent()))
    }
    val landingReturn = path("landing-close").get.handler {
        HttpResponse.Companion.html(content = HtmlDefaults.basePage("""
            <h2>Return to the App</h2>
            <p>You may now close this window and return to the app.</p>
        """.trimIndent()))
    }
    val buySubscription = path.get.handler {
        webhookConfig.await()
        val user = it.user<User>()
        val session = Session.create(
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setCustomerEmail(user.email)
                .setSuccessUrl(landingSuccess.path.fullUrl())
                .setCancelUrl(landingReturn.path.fullUrl())
                .putMetadata("user", user._id.toString())
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1)
                        .setPrice(subscriptionPricing.await().id)
                        .build()
                )
                .build()
        )
        HttpResponse.redirectToGet(session.url)
    }
    val portal = path.path("portal").get.handler {
        val user = it.user<User>()
        user.customerId?.let {
            val session = com.stripe.model.billingportal.Session.create(com.stripe.param.billingportal.SessionCreateParams.builder()
                .setReturnUrl(landingReturn.path.fullUrl())
                .setCustomer(it)
                .build())
            HttpResponse.redirectToGet(session.url)
        } ?: throw NotFoundException("No stripe configuration found for ${user.email}")
    }

    val webhook: HttpEndpoint = path.path("webhook").post.handler {
        val hook = try {
            val raw = it.body!!.text()
            Webhook.constructEvent(raw, it.headers["Stripe-Signature"], webhookConfig.await().secret)
        } catch (e: SignatureVerificationException) {
            e.printStackTrace()
            return@handler HttpResponse.plainText("Invalid", HttpStatus.BadRequest)
        }
        when (hook.type) {
            "checkout.session.completed" -> {
                val session =
                    hook.dataObjectDeserializer.`object`.let { if (it.isPresent) it.get() else null } as Session
                val userId = UUID.fromString(session.metadata["user"])
                val user = Server.users.collection().get(userId) ?: throw NotFoundException("User $userId not found")
                Server.users.collection().updateOneById(userId, modification {
                    (it.subscriptionId assign session.subscription) then (it.customerId assign session.customer)
                })
            }

            "invoice.payment_failed" -> {
                val invoice =
                    hook.dataObjectDeserializer.`object`.let { if (it.isPresent) it.get() else null } as Invoice
                Server.users.collection().updateOne(
                    condition = condition { it.subscriptionId eq invoice.subscription },
                    modification = modification { it.subscriptionId assign null }
                )
            }
        }
        HttpResponse.plainText("OK")
    }

    val regularCheck = schedule("paymentCheck", Duration.ofDays(1)) {
        Server.users.collection().find(condition { it.subscriptionId neq null }).collectChunked(15) {
            checkSomeUsers(it)
        }
    }
    val checkSomeUsers = task<List<User>>("checkSomeUsers") { users ->
        for(it in users) {
            val subscription = Subscription.retrieve(it.subscriptionId!!)
            //active, canceled, incomplete, incomplete_expired, past_due, trialing, or unpaid.
            val status = Subscription.retrieve(it.subscriptionId!!).status
            when (status) {
                "past_due", "unpaid", "canceled" -> {
                    Server.users.collection().updateOneById(it._id, modification { it.subscriptionId assign null })
                }
                "cancelled" -> {
                    if(System.currentTimeMillis() > subscription.endedAt) {
                        Server.users.collection().updateOneById(it._id, modification { it.subscriptionId assign null })
                    }
                }
            }
        }
    }
}
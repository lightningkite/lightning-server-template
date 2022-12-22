package com.lightningkite.template.stripe

import com.lightningkite.lightningdb.DatabaseModel
import com.lightningkite.lightningdb.HasId
import kotlinx.serialization.Serializable


@Serializable
@DatabaseModel
data class StripeWebhookSecret(
    override val _id: String,
    val secret: String
): HasId<String>
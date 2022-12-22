package com.lightningkite.template

import com.lightningkite.lightningdb.*
import com.lightningkite.lightningserver.HtmlDefaults
import com.lightningkite.lightningserver.auth.*
import com.lightningkite.lightningserver.cache.CacheSettings
import com.lightningkite.lightningserver.core.ServerPath
import com.lightningkite.lightningserver.core.ServerPathGroup
import com.lightningkite.lightningserver.db.DatabaseSettings
import com.lightningkite.lightningserver.db.DynamoDbCache
import com.lightningkite.lightningserver.email.EmailSettings
import com.lightningkite.lightningserver.exceptions.NotFoundException
import com.lightningkite.lightningserver.exceptions.SentryExceptionReporter
import com.lightningkite.lightningserver.files.FilesSettings
import com.lightningkite.lightningserver.files.S3FileSystem
import com.lightningkite.lightningserver.files.UploadEarlyEndpoint
import com.lightningkite.lightningserver.http.*
import com.lightningkite.lightningserver.meta.metaEndpoints
import com.lightningkite.lightningserver.notifications.NotificationSettings
import com.lightningkite.lightningserver.serialization.Serialization
import com.lightningkite.lightningserver.settings.generalSettings
import com.lightningkite.lightningserver.settings.setting
import com.lightningkite.lightningserver.tasks.Tasks
import com.lightningkite.lightningserver.tasks.doOnce
import com.lightningkite.template.stripe.StripeCredentials
import com.lightningkite.template.stripe.StripeEndpoints
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.serialization.serializer
import java.net.URLEncoder
import java.util.*

object Server : ServerPathGroup(ServerPath.root) {

    val files = setting(name = "files", default = FilesSettings())
    val database = setting(name = "database", default = DatabaseSettings())
    val email = setting(name = "email", default = EmailSettings())
    val cache = setting(name = "cache", default = CacheSettings())
    val userSigner = setting(name = "jwt", default = JwtSigner())
    val notifications = setting("notifications", default = NotificationSettings("console"))
    val stripe = setting("stripe", default = StripeCredentials("", ""))

    init {
        MongoDatabase
        DynamoDbCache
        SentryExceptionReporter
        S3FileSystem
        prepareModels()
        Tasks.onSettingsReady {
            files()
        }
    }

    val uploadEarly = UploadEarlyEndpoint(path("upload-early"), files, database, userSigner)

    val index = get.handler {
        val user = it.user<User>()
        val token = userSigner().token(user._id)
        HttpResponse.plainText("Welcome, $user!")
    }

    val auth = AuthEndpoints(path("auth"))
    val meta = path("meta").metaEndpoints<User> { it.isSuperUser }
    val users = UserEndpoints(path("users"))
    val payment = StripeEndpoints(path("payment"))

    val situate = path("situate").get.handler {
        val admin = User(
            _id = UUID.fromString("aefe4e8b-4146-437f-a827-03f5954d74f7"),
            email = "joseph@lightningkite.com",
            isSuperUser = true
        )
        if (users.collection().get(admin._id) == null)
            users.collection().upsertOneById(admin._id, admin)
        HttpResponse.plainText("OK")
    }
}
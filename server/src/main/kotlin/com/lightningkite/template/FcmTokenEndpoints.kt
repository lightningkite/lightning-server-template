package com.lightningkite.template

import com.lightningkite.lightningdb.*
import com.lightningkite.lightningserver.core.ServerPath
import com.lightningkite.lightningserver.core.ServerPathGroup
import com.lightningkite.lightningserver.db.*
import com.lightningkite.lightningserver.http.HttpStatus
import com.lightningkite.lightningserver.http.get
import com.lightningkite.lightningserver.notifications.Notification
import com.lightningkite.lightningserver.notifications.NotificationData
import com.lightningkite.lightningserver.typed.typed
import kotlinx.coroutines.flow.toList
import java.util.*

class FcmTokenEndpoints(path: ServerPath) : ServerPathGroup(path), ModelInfoWithDefault<User, FcmToken, String> {

    private val collection: FieldCollection<FcmToken> by lazy {
        Server.database().collection()
    }
    override val serialization: ModelSerializationInfo<User, FcmToken, String> = ModelSerializationInfo()
    override fun collection(): FieldCollection<FcmToken> = collection
    override suspend fun defaultItem(user: User): FcmToken = FcmToken(_id = "", user = user._id)

    override suspend fun collection(principal: User): FieldCollection<FcmToken> {
        val admin: Condition<FcmToken> = if(principal.isSuperUser) Condition.Always() else Condition.Never()
        val myTokens: Condition<FcmToken> = condition { it.user eq principal._id }

        return collection.withPermissions(
            ModelPermissions(
                create = myTokens or admin,
                read = myTokens or admin,
                update = myTokens or admin,
                delete = myTokens or admin,
            )
        )
    }

    val rest = ModelRestEndpoints(path("rest"), this)
    val restWebsocket = path("rest").restApiWebsocket(Server.database, this)
    val admin = ModelAdminEndpoints(path("admin"), this)

    val test = path("test").get.typed(
        summary = "Test Notifications",
        description = "Sends a test notification to all of your devices.",
        errorCases = listOf(),
        successCode = HttpStatus.NoContent,
        implementation = { user: User, input: Unit ->
            notify(listOf(user._id), NotificationData(Notification(
                title = "Test Notification",
                body = "You have requested a test notification.  Here it is!"
            )))
        }
    )

    suspend fun notify(users: Condition<User>, data: NotificationData) {
        Server.users.collection().find(users).collectChunked(100) {
            notify(it.map { it._id }, data)
        }
    }
    suspend fun notify(users: List<UUID>, data: NotificationData) {
        val tokens = collection().find(condition { t -> t.user inside users }).toList().map { it._id }
        Server.notifications().send(tokens, data)
    }
}
package com.lightningkite.template

import com.lightningkite.lightningdb.*
import com.lightningkite.lightningserver.core.ServerPath
import com.lightningkite.lightningserver.core.ServerPathGroup
import com.lightningkite.lightningserver.db.*
import java.util.*

class UserEndpoints(path: ServerPath) : ServerPathGroup(path), ModelInfoWithDefault<User?, User, UUID> {

    private val collection: FieldCollection<User> by lazy {
        Server.database().collection()
    }
    override val serialization: ModelSerializationInfo<User?, User, UUID> = ModelSerializationInfo()
    override fun collection(): FieldCollection<User> = collection
    override suspend fun defaultItem(user: User?): User = User(email = "")

    override suspend fun collection(principal: User?): FieldCollection<User> {
        val admin: Condition<User> = if(principal?.isSuperUser == true) Condition.Always() else Condition.Never()
        val newUser = if (principal == null) Condition.Always<User>() else Condition.Never()
        val self: Condition<User> = if(principal != null) condition { it._id eq principal._id } else Condition.Never()

        return collection.withPermissions(
            ModelPermissions(
                create = newUser or admin,
                read = admin or self,
                update = admin or self,
                delete = admin or self,
            )
        )
    }

    val rest = ModelRestEndpoints(path("rest"), this)
    val restWebsocket = path("rest").restApiWebsocket(Server.database, this)
    val admin = ModelAdminEndpoints(path("admin"), this)
}
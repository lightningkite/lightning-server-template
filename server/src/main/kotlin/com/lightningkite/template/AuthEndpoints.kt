package com.lightningkite.template

import com.lightningkite.lightningdb.collection
import com.lightningkite.lightningserver.auth.BaseAuthEndpoints
import com.lightningkite.lightningserver.auth.EmailAuthEndpoints
import com.lightningkite.lightningserver.auth.userEmailAccess
import com.lightningkite.lightningserver.core.ServerPath
import com.lightningkite.lightningserver.core.ServerPathGroup
import com.lightningkite.lightningserver.db.ModelInfo
import java.util.*

class AuthEndpoints(path: ServerPath): ServerPathGroup(path) {
    val info = ModelInfo<User, User, UUID>(
        getCollection = { Server.database().collection<User>() },
        forUser = { this }
    )
    val emailAccess = info.userEmailAccess { User(email = it) }
    val baseAuth = BaseAuthEndpoints(path, emailAccess, Server.userSigner)
    val emailAuth = EmailAuthEndpoints(baseAuth, emailAccess, Server.cache, Server.email)
}

@file:SharedCode
package com.lightningkite.template.api

import com.lightningkite.khrysalis.SharedCode
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.Observable
import com.lightningkite.rx.okhttp.*
import com.lightningkite.lightningdb.*
import com.lightningkite.lightningdb.live.*
import java.util.UUID
import java.util.Optional
import java.time.*
import com.lightningkite.lightningserver.files.UploadInformation
import com.lightningkite.lightningserver.serverhealth.ServerHealth
import kotlin.String
import com.lightningkite.template.User
import kotlin.Unit
import com.lightningkite.lightningserver.auth.EmailPinLogin
import com.lightningkite.lightningdb.Query
import com.lightningkite.lightningdb.MassModification
import kotlin.Int
import com.lightningkite.lightningdb.Modification
import com.lightningkite.lightningdb.EntryChange
import com.lightningkite.lightningdb.Condition
import com.lightningkite.lightningdb.GroupCountQuery
import com.lightningkite.lightningdb.AggregateQuery
import kotlin.Double
import com.lightningkite.lightningdb.GroupAggregateQuery
import com.lightningkite.lightningdb.ListChange

interface Api {
    val auth: AuthApi
    val user: UserApi
    fun uploadFileForRequest(): Single<UploadInformation>
    fun getServerHealth(userToken: String): Single<ServerHealth>
    interface AuthApi {
        fun refreshToken(userToken: String): Single<String>
        fun getSelf(userToken: String): Single<User>
        fun emailLoginLink(input: String): Single<Unit>
        fun emailPINLogin(input: EmailPinLogin): Single<String>
    }
    interface UserApi {
        fun query(input: Query<User>, userToken: String?): Single<List<User>>
        fun detail(id: UUID, userToken: String?): Single<User>
        fun insertBulk(input: List<User>, userToken: String?): Single<List<User>>
        fun insert(input: User, userToken: String?): Single<User>
        fun upsert(id: UUID, input: User, userToken: String?): Single<User>
        fun bulkReplace(input: List<User>, userToken: String?): Single<List<User>>
        fun replace(id: UUID, input: User, userToken: String?): Single<User>
        fun bulkModify(input: MassModification<User>, userToken: String?): Single<Int>
        fun modifyWithDiff(id: UUID, input: Modification<User>, userToken: String?): Single<EntryChange<User>>
        fun modify(id: UUID, input: Modification<User>, userToken: String?): Single<User>
        fun bulkDelete(input: Condition<User>, userToken: String?): Single<Int>
        fun delete(id: UUID, userToken: String?): Single<Unit>
        fun count(input: Condition<User>, userToken: String?): Single<Int>
        fun groupCount(input: GroupCountQuery<User>, userToken: String?): Single<Map<String, Int>>
        fun aggregate(input: AggregateQuery<User>, userToken: String?): Single<Optional<Double>>
        fun groupAggregate(input: GroupAggregateQuery<User>, userToken: String?): Single<Map<String, Double?>>
        fun watch(userToken: String?): Observable<WebSocketIsh<ListChange<User>, Query<User>>>
    }
}


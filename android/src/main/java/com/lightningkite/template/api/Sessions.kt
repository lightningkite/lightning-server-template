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
import kotlin.String
import kotlin.Unit
import com.lightningkite.lightningserver.auth.EmailPinLogin
import com.lightningkite.template.User
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
import com.lightningkite.lightningserver.serverhealth.ServerHealth

open class AbstractAnonymousSession(val api: Api) {
    val auth: AbstractAnonymousSessionAuthApi = AbstractAnonymousSessionAuthApi(api.auth)
    val user: AbstractAnonymousSessionUserApi = AbstractAnonymousSessionUserApi(api.user)
    fun uploadFileForRequest(): Single<UploadInformation> = api.uploadFileForRequest()
    open class AbstractAnonymousSessionAuthApi(val api: Api.AuthApi) {
        fun anonymousToken(): Single<String> = api.anonymousToken(null)
        fun emailLoginLink(input: String): Single<Unit> = api.emailLoginLink(input)
        fun emailPINLogin(input: EmailPinLogin): Single<String> = api.emailPINLogin(input)
    }
    open class AbstractAnonymousSessionUserApi(val api: Api.UserApi) {
        fun default(): Single<User> = api.default(null)
        fun query(input: Query<User>): Single<List<User>> = api.query(input, null)
        fun detail(id: UUID): Single<User> = api.detail(id, null)
        fun insertBulk(input: List<User>): Single<List<User>> = api.insertBulk(input, null)
        fun insert(input: User): Single<User> = api.insert(input, null)
        fun upsert(id: UUID, input: User): Single<User> = api.upsert(id, input, null)
        fun bulkReplace(input: List<User>): Single<List<User>> = api.bulkReplace(input, null)
        fun replace(id: UUID, input: User): Single<User> = api.replace(id, input, null)
        fun bulkModify(input: MassModification<User>): Single<Int> = api.bulkModify(input, null)
        fun modifyWithDiff(id: UUID, input: Modification<User>): Single<EntryChange<User>> = api.modifyWithDiff(id, input, null)
        fun modify(id: UUID, input: Modification<User>): Single<User> = api.modify(id, input, null)
        fun bulkDelete(input: Condition<User>): Single<Int> = api.bulkDelete(input, null)
        fun delete(id: UUID): Single<Unit> = api.delete(id, null)
        fun count(input: Condition<User>): Single<Int> = api.count(input, null)
        fun groupCount(input: GroupCountQuery<User>): Single<Map<String, Int>> = api.groupCount(input, null)
        fun aggregate(input: AggregateQuery<User>): Single<Optional<Double>> = api.aggregate(input, null)
        fun groupAggregate(input: GroupAggregateQuery<User>): Single<Map<String, Double?>> = api.groupAggregate(input, null)
        fun watch(): Observable<WebSocketIsh<ListChange<User>, Query<User>>> = api.watch(null)
    }
}

abstract class AbstractUserSession(api: Api, userToken: String) {
    abstract val api: Api
    abstract val userToken: String
    val auth: UserSessionAuthApi = UserSessionAuthApi(api.auth, userToken)
    val user: UserSessionUserApi = UserSessionUserApi(api.user, userToken)
    fun uploadFileForRequest(): Single<UploadInformation> = api.uploadFileForRequest()
    fun getServerHealth(): Single<ServerHealth> = api.getServerHealth(userToken)
    class UserSessionAuthApi(val api: Api.AuthApi, val userToken: String) {
        fun refreshToken(): Single<String> = api.refreshToken(userToken)
        fun getSelf(): Single<User> = api.getSelf(userToken)
        fun anonymousToken(): Single<String> = api.anonymousToken(userToken)
        fun emailLoginLink(input: String): Single<Unit> = api.emailLoginLink(input)
        fun emailPINLogin(input: EmailPinLogin): Single<String> = api.emailPINLogin(input)
    }
    class UserSessionUserApi(val api: Api.UserApi, val userToken: String) {
        fun default(): Single<User> = api.default(userToken)
        fun query(input: Query<User>): Single<List<User>> = api.query(input, userToken)
        fun detail(id: UUID): Single<User> = api.detail(id, userToken)
        fun insertBulk(input: List<User>): Single<List<User>> = api.insertBulk(input, userToken)
        fun insert(input: User): Single<User> = api.insert(input, userToken)
        fun upsert(id: UUID, input: User): Single<User> = api.upsert(id, input, userToken)
        fun bulkReplace(input: List<User>): Single<List<User>> = api.bulkReplace(input, userToken)
        fun replace(id: UUID, input: User): Single<User> = api.replace(id, input, userToken)
        fun bulkModify(input: MassModification<User>): Single<Int> = api.bulkModify(input, userToken)
        fun modifyWithDiff(id: UUID, input: Modification<User>): Single<EntryChange<User>> = api.modifyWithDiff(id, input, userToken)
        fun modify(id: UUID, input: Modification<User>): Single<User> = api.modify(id, input, userToken)
        fun bulkDelete(input: Condition<User>): Single<Int> = api.bulkDelete(input, userToken)
        fun delete(id: UUID): Single<Unit> = api.delete(id, userToken)
        fun count(input: Condition<User>): Single<Int> = api.count(input, userToken)
        fun groupCount(input: GroupCountQuery<User>): Single<Map<String, Int>> = api.groupCount(input, userToken)
        fun aggregate(input: AggregateQuery<User>): Single<Optional<Double>> = api.aggregate(input, userToken)
        fun groupAggregate(input: GroupAggregateQuery<User>): Single<Map<String, Double?>> = api.groupAggregate(input, userToken)
        fun watch(): Observable<WebSocketIsh<ListChange<User>, Query<User>>> = api.watch(userToken)
    }
}


@file:SharedCode
package com.lightningkite.template.api

import com.lightningkite.khrysalis.SharedCode
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.Observable
import com.lightningkite.rx.android.resources.ImageReference
import com.lightningkite.rx.kotlin
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

class LiveApi(val httpUrl: String, val socketUrl: String): Api {
    override val auth: Api.AuthApi = LiveAuthApi(httpUrl = httpUrl, socketUrl = socketUrl)
    override val user: Api.UserApi = LiveUserApi(httpUrl = httpUrl, socketUrl = socketUrl)
    override fun uploadFileForRequest(): Single<UploadInformation> = HttpClient.call(
        url = "$httpUrl/upload-early",
        method = HttpClient.GET,
    ).readJson()
    override fun getServerHealth(userToken: String): Single<ServerHealth> = HttpClient.call(
        url = "$httpUrl/meta/health",
        method = HttpClient.GET,
        headers = mapOf("Authorization" to "Bearer $userToken"),
    ).readJson()
    class LiveAuthApi(val httpUrl: String, val socketUrl: String): Api.AuthApi {
        override fun refreshToken(userToken: String): Single<String> = HttpClient.call(
            url = "$httpUrl/auth/refresh-token",
            method = HttpClient.GET,
            headers = mapOf("Authorization" to "Bearer $userToken"),
        ).readJson()
        override fun getSelf(userToken: String): Single<User> = HttpClient.call(
            url = "$httpUrl/auth/self",
            method = HttpClient.GET,
            headers = mapOf("Authorization" to "Bearer $userToken"),
        ).readJson()
        override fun emailLoginLink(input: String): Single<Unit> = HttpClient.call(
            url = "$httpUrl/auth/login-email",
            method = HttpClient.POST,
            body = input.toJsonRequestBody()
        ).discard()
        override fun emailPINLogin(input: EmailPinLogin): Single<String> = HttpClient.call(
            url = "$httpUrl/auth/login-email-pin",
            method = HttpClient.POST,
            body = input.toJsonRequestBody()
        ).readJson()
    }
    class LiveUserApi(val httpUrl: String, val socketUrl: String): Api.UserApi {
        override fun query(input: Query<User>, userToken: String?): Single<List<User>> = HttpClient.call(
            url = "$httpUrl/users/rest/query",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun detail(id: UUID, userToken: String?): Single<User> = HttpClient.call(
            url = "$httpUrl/users/rest/${id}",
            method = HttpClient.GET,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
        ).readJson()
        override fun insertBulk(input: List<User>, userToken: String?): Single<List<User>> = HttpClient.call(
            url = "$httpUrl/users/rest/bulk",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun insert(input: User, userToken: String?): Single<User> = HttpClient.call(
            url = "$httpUrl/users/rest",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun upsert(id: UUID, input: User, userToken: String?): Single<User> = HttpClient.call(
            url = "$httpUrl/users/rest/${id}",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun bulkReplace(input: List<User>, userToken: String?): Single<List<User>> = HttpClient.call(
            url = "$httpUrl/users/rest",
            method = HttpClient.PUT,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun replace(id: UUID, input: User, userToken: String?): Single<User> = HttpClient.call(
            url = "$httpUrl/users/rest/${id}",
            method = HttpClient.PUT,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun bulkModify(input: MassModification<User>, userToken: String?): Single<Int> = HttpClient.call(
            url = "$httpUrl/users/rest/bulk",
            method = HttpClient.PATCH,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun modifyWithDiff(id: UUID, input: Modification<User>, userToken: String?): Single<EntryChange<User>> = HttpClient.call(
            url = "$httpUrl/users/rest/${id}/delta",
            method = HttpClient.PATCH,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun modify(id: UUID, input: Modification<User>, userToken: String?): Single<User> = HttpClient.call(
            url = "$httpUrl/users/rest/${id}",
            method = HttpClient.PATCH,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun bulkDelete(input: Condition<User>, userToken: String?): Single<Int> = HttpClient.call(
            url = "$httpUrl/users/rest/bulk-delete",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun delete(id: UUID, userToken: String?): Single<Unit> = HttpClient.call(
            url = "$httpUrl/users/rest/${id}",
            method = HttpClient.DELETE,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
        ).discard()
        override fun count(input: Condition<User>, userToken: String?): Single<Int> = HttpClient.call(
            url = "$httpUrl/users/rest/count",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun groupCount(input: GroupCountQuery<User>, userToken: String?): Single<Map<String, Int>> = HttpClient.call(
            url = "$httpUrl/users/rest/group-count",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun aggregate(input: AggregateQuery<User>, userToken: String?): Single<Optional<Double>> = HttpClient.call(
            url = "$httpUrl/users/rest/aggregate",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun groupAggregate(input: GroupAggregateQuery<User>, userToken: String?): Single<Map<String, Double?>> = HttpClient.call(
            url = "$httpUrl/users/rest/group-aggregate",
            method = HttpClient.POST,
            headers = if(userToken != null) mapOf("Authorization" to "Bearer $userToken") else mapOf(),
            body = input.toJsonRequestBody()
        ).readJson()
        override fun watch(userToken: String?): Observable<WebSocketIsh<ListChange<User>, Query<User>>> = multiplexedSocket(
            url = "$socketUrl?path=multiplex", 
            path = "/users/rest", 
            queryParams = if(userToken != null) mapOf("jwt" to listOf(userToken)) else mapOf()
        )
    }
}


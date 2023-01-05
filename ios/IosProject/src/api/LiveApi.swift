// Package: com.lightningkite.template.api
// Generated by Khrysalis - this file will be overwritten.
import KhrysalisRuntime
import RxSwift
import LightningServer
import RxSwiftPlus
import Foundation

public final class LiveApi : Api {
    public var httpUrl: String
    public var socketUrl: String
    public init(httpUrl: String, socketUrl: String) {
        self.httpUrl = httpUrl
        self.socketUrl = socketUrl
        self._auth = LiveAuthApi(httpUrl: httpUrl, socketUrl: socketUrl)
        self._user = LiveUserApi(httpUrl: httpUrl, socketUrl: socketUrl)
        self._fcmToken = LiveFcmTokenApi(httpUrl: httpUrl, socketUrl: socketUrl)
        //Necessary properties should be initialized now
    }
    
    public var _auth: ApiAuthApi
    public var auth: ApiAuthApi {
        get { return _auth }
    }
    public var _user: ApiUserApi
    public var user: ApiUserApi {
        get { return _user }
    }
    public var _fcmToken: ApiFcmTokenApi
    public var fcmToken: ApiFcmTokenApi {
        get { return _fcmToken }
    }
    public func uploadFileForRequest() -> Single<UploadInformation> {
        return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/upload-early", method: HttpClient.INSTANCE.GET).readJson();
    }
    public func getServerHealth(userToken: String) -> Single<ServerHealth> {
        return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/meta/health", method: HttpClient.INSTANCE.GET, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))"))).readJson();
    }
    public final class LiveAuthApi : ApiAuthApi {
        public var httpUrl: String
        public var socketUrl: String
        public init(httpUrl: String, socketUrl: String) {
            self.httpUrl = httpUrl
            self.socketUrl = socketUrl
            //Necessary properties should be initialized now
        }
        
        public func refreshToken(userToken: String) -> Single<String> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/auth/refresh-token", method: HttpClient.INSTANCE.GET, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))"))).readJson();
        }
        public func getSelf(userToken: String) -> Single<User> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/auth/self", method: HttpClient.INSTANCE.GET, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))"))).readJson();
        }
        public func anonymousToken(userToken: String?) -> Single<String> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/auth/anonymous", method: HttpClient.INSTANCE.GET, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf()).readJson();
        }
        public func emailLoginLink(input: String) -> Single<Void> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/auth/login-email", method: HttpClient.INSTANCE.POST, body: input.toJsonRequestBody()).discard();
        }
        public func emailPINLogin(input: EmailPinLogin) -> Single<String> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/auth/login-email-pin", method: HttpClient.INSTANCE.POST, body: input.toJsonRequestBody()).readJson();
        }
    }
    public final class LiveUserApi : ApiUserApi {
        public var httpUrl: String
        public var socketUrl: String
        public init(httpUrl: String, socketUrl: String) {
            self.httpUrl = httpUrl
            self.socketUrl = socketUrl
            //Necessary properties should be initialized now
        }
        
        public func `default`(userToken: String?) -> Single<User> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/_default_", method: HttpClient.INSTANCE.GET, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf()).readJson();
        }
        public func query(input: Query<User>, userToken: String?) -> Single<Array<User>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/query", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func detail(id: UUID, userToken: String?) -> Single<User> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/\(id)", method: HttpClient.INSTANCE.GET, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf()).readJson();
        }
        public func insertBulk(input: Array<User>, userToken: String?) -> Single<Array<User>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/bulk", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func insert(input: User, userToken: String?) -> Single<User> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func upsert(id: UUID, input: User, userToken: String?) -> Single<User> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/\(id)", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func bulkReplace(input: Array<User>, userToken: String?) -> Single<Array<User>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest", method: HttpClient.INSTANCE.PUT, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func replace(id: UUID, input: User, userToken: String?) -> Single<User> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/\(id)", method: HttpClient.INSTANCE.PUT, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func bulkModify(input: MassModification<User>, userToken: String?) -> Single<Int> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/bulk", method: HttpClient.INSTANCE.PATCH, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func modifyWithDiff(id: UUID, input: Modification<User>, userToken: String?) -> Single<EntryChange<User>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/\(id)/delta", method: HttpClient.INSTANCE.PATCH, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func modify(id: UUID, input: Modification<User>, userToken: String?) -> Single<User> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/\(id)", method: HttpClient.INSTANCE.PATCH, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func bulkDelete(input: Condition<User>, userToken: String?) -> Single<Int> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/bulk-delete", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func delete(id: UUID, userToken: String?) -> Single<Void> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/\(id)", method: HttpClient.INSTANCE.DELETE, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf()).discard();
        }
        public func count(input: Condition<User>, userToken: String?) -> Single<Int> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/count", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func groupCount(input: GroupCountQuery<User>, userToken: String?) -> Single<Dictionary<String, Int>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/group-count", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func aggregate(input: AggregateQuery<User>, userToken: String?) -> Single<Double?> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/aggregate", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func groupAggregate(input: GroupAggregateQuery<User>, userToken: String?) -> Single<Dictionary<String, Double?>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/users/rest/group-aggregate", method: HttpClient.INSTANCE.POST, headers: userToken != nil ? dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")) : dictionaryOf(), body: input.toJsonRequestBody()).readJson();
        }
        public func watch(userToken: String?) -> Observable<WebSocketIsh<ListChange<User>, Query<User>>> {
            return multiplexedSocket(url: "\(String(kotlin: self.socketUrl))?path=multiplex", path: "/users/rest", queryParams: userToken != nil ? dictionaryOf(Pair("jwt", [userToken!])) : dictionaryOf());
        }
    }
    public final class LiveFcmTokenApi : ApiFcmTokenApi {
        public var httpUrl: String
        public var socketUrl: String
        public init(httpUrl: String, socketUrl: String) {
            self.httpUrl = httpUrl
            self.socketUrl = socketUrl
            //Necessary properties should be initialized now
        }
        
        public func `default`(userToken: String) -> Single<FcmToken> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/_default_", method: HttpClient.INSTANCE.GET, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))"))).readJson();
        }
        public func query(input: Query<FcmToken>, userToken: String) -> Single<Array<FcmToken>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/query", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func detail(id: String, userToken: String) -> Single<FcmToken> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/\(String(kotlin: id))", method: HttpClient.INSTANCE.GET, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))"))).readJson();
        }
        public func insertBulk(input: Array<FcmToken>, userToken: String) -> Single<Array<FcmToken>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/bulk", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func insert(input: FcmToken, userToken: String) -> Single<FcmToken> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func upsert(id: String, input: FcmToken, userToken: String) -> Single<FcmToken> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/\(String(kotlin: id))", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func bulkReplace(input: Array<FcmToken>, userToken: String) -> Single<Array<FcmToken>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest", method: HttpClient.INSTANCE.PUT, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func replace(id: String, input: FcmToken, userToken: String) -> Single<FcmToken> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/\(String(kotlin: id))", method: HttpClient.INSTANCE.PUT, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func bulkModify(input: MassModification<FcmToken>, userToken: String) -> Single<Int> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/bulk", method: HttpClient.INSTANCE.PATCH, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func modifyWithDiff(id: String, input: Modification<FcmToken>, userToken: String) -> Single<EntryChange<FcmToken>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/\(String(kotlin: id))/delta", method: HttpClient.INSTANCE.PATCH, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func modify(id: String, input: Modification<FcmToken>, userToken: String) -> Single<FcmToken> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/\(String(kotlin: id))", method: HttpClient.INSTANCE.PATCH, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func bulkDelete(input: Condition<FcmToken>, userToken: String) -> Single<Int> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/bulk-delete", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func delete(id: String, userToken: String) -> Single<Void> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/\(String(kotlin: id))", method: HttpClient.INSTANCE.DELETE, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))"))).discard();
        }
        public func count(input: Condition<FcmToken>, userToken: String) -> Single<Int> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/count", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func groupCount(input: GroupCountQuery<FcmToken>, userToken: String) -> Single<Dictionary<String, Int>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/group-count", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func aggregate(input: AggregateQuery<FcmToken>, userToken: String) -> Single<Double?> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/aggregate", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func groupAggregate(input: GroupAggregateQuery<FcmToken>, userToken: String) -> Single<Dictionary<String, Double?>> {
            return HttpClient.INSTANCE.call(url: "\(String(kotlin: self.httpUrl))/fcm-tokens/rest/group-aggregate", method: HttpClient.INSTANCE.POST, headers: dictionaryOf(Pair("Authorization", "Bearer \(String(kotlin: userToken))")), body: input.toJsonRequestBody()).readJson();
        }
        public func watch(userToken: String) -> Observable<WebSocketIsh<ListChange<FcmToken>, Query<FcmToken>>> {
            return multiplexedSocket(url: "\(String(kotlin: self.socketUrl))?path=multiplex", path: "/fcm-tokens/rest", queryParams: dictionaryOf(Pair("jwt", [userToken])));
        }
    }
}



// Package: com.lightningkite.template.api
// Generated by Khrysalis - this file will be overwritten.
import KhrysalisRuntime
import RxSwift
import LightningServer
import RxSwiftPlus
import Foundation

public protocol Api: AnyObject {
    
    var auth: ApiAuthApi { get }
    
    var user: ApiUserApi { get }
    
    var fcmToken: ApiFcmTokenApi { get }
    
    func uploadFileForRequest() -> Single<UploadInformation>
    func getServerHealth(userToken: String) -> Single<ServerHealth>
    
    
    
}



public protocol ApiAuthApi: AnyObject {
    
    func refreshToken(userToken: String) -> Single<String>
    func getSelf(userToken: String) -> Single<User>
    func anonymousToken(userToken: String?) -> Single<String>
    func emailLoginLink(input: String) -> Single<Void>
    func emailPINLogin(input: EmailPinLogin) -> Single<String>
}

public protocol ApiUserApi: AnyObject {
    
    func `default`(userToken: String?) -> Single<User>
    func query(input: Query<User>, userToken: String?) -> Single<Array<User>>
    func detail(id: UUID, userToken: String?) -> Single<User>
    func insertBulk(input: Array<User>, userToken: String?) -> Single<Array<User>>
    func insert(input: User, userToken: String?) -> Single<User>
    func upsert(id: UUID, input: User, userToken: String?) -> Single<User>
    func bulkReplace(input: Array<User>, userToken: String?) -> Single<Array<User>>
    func replace(id: UUID, input: User, userToken: String?) -> Single<User>
    func bulkModify(input: MassModification<User>, userToken: String?) -> Single<Int>
    func modifyWithDiff(id: UUID, input: Modification<User>, userToken: String?) -> Single<EntryChange<User>>
    func modify(id: UUID, input: Modification<User>, userToken: String?) -> Single<User>
    func bulkDelete(input: Condition<User>, userToken: String?) -> Single<Int>
    func delete(id: UUID, userToken: String?) -> Single<Void>
    func count(input: Condition<User>, userToken: String?) -> Single<Int>
    func groupCount(input: GroupCountQuery<User>, userToken: String?) -> Single<Dictionary<String, Int>>
    func aggregate(input: AggregateQuery<User>, userToken: String?) -> Single<Double?>
    func groupAggregate(input: GroupAggregateQuery<User>, userToken: String?) -> Single<Dictionary<String, Double?>>
    func watch(userToken: String?) -> Observable<WebSocketIsh<ListChange<User>, Query<User>>>
}

public protocol ApiFcmTokenApi: AnyObject {
    
    func `default`(userToken: String) -> Single<FcmToken>
    func query(input: Query<FcmToken>, userToken: String) -> Single<Array<FcmToken>>
    func detail(id: String, userToken: String) -> Single<FcmToken>
    func insertBulk(input: Array<FcmToken>, userToken: String) -> Single<Array<FcmToken>>
    func insert(input: FcmToken, userToken: String) -> Single<FcmToken>
    func upsert(id: String, input: FcmToken, userToken: String) -> Single<FcmToken>
    func bulkReplace(input: Array<FcmToken>, userToken: String) -> Single<Array<FcmToken>>
    func replace(id: String, input: FcmToken, userToken: String) -> Single<FcmToken>
    func bulkModify(input: MassModification<FcmToken>, userToken: String) -> Single<Int>
    func modifyWithDiff(id: String, input: Modification<FcmToken>, userToken: String) -> Single<EntryChange<FcmToken>>
    func modify(id: String, input: Modification<FcmToken>, userToken: String) -> Single<FcmToken>
    func bulkDelete(input: Condition<FcmToken>, userToken: String) -> Single<Int>
    func delete(id: String, userToken: String) -> Single<Void>
    func count(input: Condition<FcmToken>, userToken: String) -> Single<Int>
    func groupCount(input: GroupCountQuery<FcmToken>, userToken: String) -> Single<Dictionary<String, Int>>
    func aggregate(input: AggregateQuery<FcmToken>, userToken: String) -> Single<Double?>
    func groupAggregate(input: GroupAggregateQuery<FcmToken>, userToken: String) -> Single<Dictionary<String, Double?>>
    func watch(userToken: String) -> Observable<WebSocketIsh<ListChange<FcmToken>, Query<FcmToken>>>
}

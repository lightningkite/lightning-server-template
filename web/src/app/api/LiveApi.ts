// Package: com.lightningkite.template.api
// Generated by Khrysalis - this file will be overwritten.
import { FcmToken, User } from '../../shared/models'
import { Api } from './Api'
import { AggregateQuery, Condition, EmailPinLogin, EntryChange, GroupAggregateQuery, GroupCountQuery, ListChange, MassModification, Modification, Query, ServerHealth, UploadInformation, WebSocketIsh, multiplexedSocketReified } from '@lightningkite/lightning-server'
import { HttpBody, HttpClient, fromJSON, unsuccessfulAsError } from '@lightningkite/rxjs-plus'
import { Observable, switchMap } from 'rxjs'

//! Declares com.lightningkite.template.api.LiveApi
export class LiveApi implements Api {
    public static implementsApi = true;
    public constructor(public readonly httpUrl: string, public readonly socketUrl: string) {
        this.auth = new LiveApi.LiveAuthApi(this.httpUrl, this.socketUrl);
        this.user = new LiveApi.LiveUserApi(this.httpUrl, this.socketUrl);
        this.fcmToken = new LiveApi.LiveFcmTokenApi(this.httpUrl, this.socketUrl);
    }
    
    public readonly auth: Api.AuthApi;
    public readonly user: Api.UserApi;
    public readonly fcmToken: Api.FcmTokenApi;
    public uploadFileForRequest(): Observable<UploadInformation> {
        return HttpClient.INSTANCE.call(`${this.httpUrl}/upload-early`, HttpClient.INSTANCE.GET, undefined, undefined, undefined).pipe(unsuccessfulAsError, fromJSON<UploadInformation>([UploadInformation]));
    }
    public getServerHealth(userToken: string): Observable<ServerHealth> {
        return HttpClient.INSTANCE.call(`${this.httpUrl}/meta/health`, HttpClient.INSTANCE.GET, new Map([["Authorization", `Bearer ${userToken}`]]), undefined, undefined).pipe(unsuccessfulAsError, fromJSON<ServerHealth>([ServerHealth]));
    }
    
    
    
}
export namespace LiveApi {
    //! Declares com.lightningkite.template.api.LiveApi.LiveAuthApi
    export class LiveAuthApi implements Api.AuthApi {
        public static implementsAuthApi = true;
        public constructor(public readonly httpUrl: string, public readonly socketUrl: string) {
        }
        
        public refreshToken(userToken: string): Observable<string> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/auth/refresh-token`, HttpClient.INSTANCE.GET, new Map([["Authorization", `Bearer ${userToken}`]]), undefined, undefined).pipe(unsuccessfulAsError, fromJSON<string>([String]));
        }
        public getSelf(userToken: string): Observable<User> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/auth/self`, HttpClient.INSTANCE.GET, new Map([["Authorization", `Bearer ${userToken}`]]), undefined, undefined).pipe(unsuccessfulAsError, fromJSON<User>([User]));
        }
        public anonymousToken(userToken: (string | null)): Observable<string> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/auth/anonymous`, HttpClient.INSTANCE.GET, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), undefined, undefined).pipe(unsuccessfulAsError, fromJSON<string>([String]));
        }
        public emailLoginLink(input: string): Observable<void> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/auth/login-email`, HttpClient.INSTANCE.POST, undefined, HttpBody.json(input), undefined).pipe(unsuccessfulAsError, switchMap(x => x.text().then(x => undefined)));
        }
        public emailPINLogin(input: EmailPinLogin): Observable<string> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/auth/login-email-pin`, HttpClient.INSTANCE.POST, undefined, HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<string>([String]));
        }
    }
}
export namespace LiveApi {
    //! Declares com.lightningkite.template.api.LiveApi.LiveUserApi
    export class LiveUserApi implements Api.UserApi {
        public static implementsUserApi = true;
        public constructor(public readonly httpUrl: string, public readonly socketUrl: string) {
        }
        
        public _default(userToken: (string | null)): Observable<User> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/_default_`, HttpClient.INSTANCE.GET, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), undefined, undefined).pipe(unsuccessfulAsError, fromJSON<User>([User]));
        }
        public query(input: Query<User>, userToken: (string | null)): Observable<Array<User>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/query`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Array<User>>([Array, [User]]));
        }
        public detail(id: string, userToken: (string | null)): Observable<User> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/${id}`, HttpClient.INSTANCE.GET, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), undefined, undefined).pipe(unsuccessfulAsError, fromJSON<User>([User]));
        }
        public insertBulk(input: Array<User>, userToken: (string | null)): Observable<Array<User>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/bulk`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Array<User>>([Array, [User]]));
        }
        public insert(input: User, userToken: (string | null)): Observable<User> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<User>([User]));
        }
        public upsert(id: string, input: User, userToken: (string | null)): Observable<User> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/${id}`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<User>([User]));
        }
        public bulkReplace(input: Array<User>, userToken: (string | null)): Observable<Array<User>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest`, HttpClient.INSTANCE.PUT, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Array<User>>([Array, [User]]));
        }
        public replace(id: string, input: User, userToken: (string | null)): Observable<User> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/${id}`, HttpClient.INSTANCE.PUT, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<User>([User]));
        }
        public bulkModify(input: MassModification<User>, userToken: (string | null)): Observable<number> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/bulk`, HttpClient.INSTANCE.PATCH, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<number>([Number]));
        }
        public modifyWithDiff(id: string, input: Modification<User>, userToken: (string | null)): Observable<EntryChange<User>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/${id}/delta`, HttpClient.INSTANCE.PATCH, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<EntryChange<User>>([EntryChange, [User]]));
        }
        public modify(id: string, input: Modification<User>, userToken: (string | null)): Observable<User> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/${id}`, HttpClient.INSTANCE.PATCH, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<User>([User]));
        }
        public bulkDelete(input: Condition<User>, userToken: (string | null)): Observable<number> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/bulk-delete`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<number>([Number]));
        }
        public _delete(id: string, userToken: (string | null)): Observable<void> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/${id}`, HttpClient.INSTANCE.DELETE, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), undefined, undefined).pipe(unsuccessfulAsError, switchMap(x => x.text().then(x => undefined)));
        }
        public count(input: Condition<User>, userToken: (string | null)): Observable<number> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/count`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<number>([Number]));
        }
        public groupCount(input: GroupCountQuery<User>, userToken: (string | null)): Observable<Map<string, number>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/group-count`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Map<string, number>>([Map, [String], [Number]]));
        }
        public aggregate(input: AggregateQuery<User>, userToken: (string | null)): Observable<(number | null)> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/aggregate`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<(number | null)>([Number]));
        }
        public groupAggregate(input: GroupAggregateQuery<User>, userToken: (string | null)): Observable<Map<string, (number | null)>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/users/rest/group-aggregate`, HttpClient.INSTANCE.POST, userToken !== null ? new Map([["Authorization", `Bearer ${userToken}`]]) : new Map([]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Map<string, (number | null)>>([Map, [String], [Number]]));
        }
        public watch(userToken: (string | null)): Observable<WebSocketIsh<ListChange<User>, Query<User>>> {
            return multiplexedSocketReified<ListChange<User>, Query<User>>([ListChange, [User]], [Query, [User]], `${this.socketUrl}?path=multiplex`, "/users/rest", userToken !== null ? new Map([["jwt", [userToken!]]]) : new Map([]));
        }
    }
}
export namespace LiveApi {
    //! Declares com.lightningkite.template.api.LiveApi.LiveFcmTokenApi
    export class LiveFcmTokenApi implements Api.FcmTokenApi {
        public static implementsFcmTokenApi = true;
        public constructor(public readonly httpUrl: string, public readonly socketUrl: string) {
        }
        
        public _default(userToken: string): Observable<FcmToken> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/_default_`, HttpClient.INSTANCE.GET, new Map([["Authorization", `Bearer ${userToken}`]]), undefined, undefined).pipe(unsuccessfulAsError, fromJSON<FcmToken>([FcmToken]));
        }
        public query(input: Query<FcmToken>, userToken: string): Observable<Array<FcmToken>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/query`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Array<FcmToken>>([Array, [FcmToken]]));
        }
        public detail(id: string, userToken: string): Observable<FcmToken> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/${id}`, HttpClient.INSTANCE.GET, new Map([["Authorization", `Bearer ${userToken}`]]), undefined, undefined).pipe(unsuccessfulAsError, fromJSON<FcmToken>([FcmToken]));
        }
        public insertBulk(input: Array<FcmToken>, userToken: string): Observable<Array<FcmToken>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/bulk`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Array<FcmToken>>([Array, [FcmToken]]));
        }
        public insert(input: FcmToken, userToken: string): Observable<FcmToken> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<FcmToken>([FcmToken]));
        }
        public upsert(id: string, input: FcmToken, userToken: string): Observable<FcmToken> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/${id}`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<FcmToken>([FcmToken]));
        }
        public bulkReplace(input: Array<FcmToken>, userToken: string): Observable<Array<FcmToken>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest`, HttpClient.INSTANCE.PUT, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Array<FcmToken>>([Array, [FcmToken]]));
        }
        public replace(id: string, input: FcmToken, userToken: string): Observable<FcmToken> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/${id}`, HttpClient.INSTANCE.PUT, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<FcmToken>([FcmToken]));
        }
        public bulkModify(input: MassModification<FcmToken>, userToken: string): Observable<number> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/bulk`, HttpClient.INSTANCE.PATCH, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<number>([Number]));
        }
        public modifyWithDiff(id: string, input: Modification<FcmToken>, userToken: string): Observable<EntryChange<FcmToken>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/${id}/delta`, HttpClient.INSTANCE.PATCH, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<EntryChange<FcmToken>>([EntryChange, [FcmToken]]));
        }
        public modify(id: string, input: Modification<FcmToken>, userToken: string): Observable<FcmToken> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/${id}`, HttpClient.INSTANCE.PATCH, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<FcmToken>([FcmToken]));
        }
        public bulkDelete(input: Condition<FcmToken>, userToken: string): Observable<number> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/bulk-delete`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<number>([Number]));
        }
        public _delete(id: string, userToken: string): Observable<void> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/${id}`, HttpClient.INSTANCE.DELETE, new Map([["Authorization", `Bearer ${userToken}`]]), undefined, undefined).pipe(unsuccessfulAsError, switchMap(x => x.text().then(x => undefined)));
        }
        public count(input: Condition<FcmToken>, userToken: string): Observable<number> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/count`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<number>([Number]));
        }
        public groupCount(input: GroupCountQuery<FcmToken>, userToken: string): Observable<Map<string, number>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/group-count`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Map<string, number>>([Map, [String], [Number]]));
        }
        public aggregate(input: AggregateQuery<FcmToken>, userToken: string): Observable<(number | null)> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/aggregate`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<(number | null)>([Number]));
        }
        public groupAggregate(input: GroupAggregateQuery<FcmToken>, userToken: string): Observable<Map<string, (number | null)>> {
            return HttpClient.INSTANCE.call(`${this.httpUrl}/fcm-tokens/rest/group-aggregate`, HttpClient.INSTANCE.POST, new Map([["Authorization", `Bearer ${userToken}`]]), HttpBody.json(input), undefined).pipe(unsuccessfulAsError, fromJSON<Map<string, (number | null)>>([Map, [String], [Number]]));
        }
        public watch(userToken: string): Observable<WebSocketIsh<ListChange<FcmToken>, Query<FcmToken>>> {
            return multiplexedSocketReified<ListChange<FcmToken>, Query<FcmToken>>([ListChange, [FcmToken]], [Query, [FcmToken]], `${this.socketUrl}?path=multiplex`, "/fcm-tokens/rest", new Map([["jwt", [userToken]]]));
        }
    }
}


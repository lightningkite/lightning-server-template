// Package: com.lightningkite.template.api
// Generated by Khrysalis - this file will be overwritten.
import { LiveApi } from './LiveApi'
import { findOr } from 'iter-tools-es'

//! Declares com.lightningkite.template.api.ServerOption
export class ServerOption {
    public constructor(public readonly name: string, public readonly api: LiveApi) {
    }
}

//! Declares com.lightningkite.template.api.ServerOptions
export class ServerOptions {
    private constructor() {
        this.staging = new ServerOption("Staging", new LiveApi("https://templateapi.cs.lightningkite.com", "wss://ws.templateapi.cs.lightningkite.com"));
        this.jivie = new ServerOption("Jivie", new LiveApi("https://jivie.lightningkite.com", "wss://jivie.lightningkite.com"));
        this.bSvedin = new ServerOption("BSvedin", new LiveApi("https://bsvedin.lightningkite.com", "wss://bsvedin.lightningkite.com"));
        this.availableServers = [this.staging, this.jivie];
    }
    public static INSTANCE = new ServerOptions();
    
    public readonly staging: ServerOption;
    public readonly jivie: ServerOption;
    public readonly bSvedin: ServerOption;
    
    public readonly availableServers: Array<ServerOption>;
    
    getOptionByName(name: string): (ServerOption | null) {
        return findOr(null, (it: ServerOption): boolean => (it.name === name), this.availableServers);
    }
    
    getIndexByName(name: string): (number | null) {
        const index = this.availableServers.findIndex((it: ServerOption): boolean => (it.name === name));
        return index >= 0 ? index : null;
    }
}
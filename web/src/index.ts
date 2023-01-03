
import {RootVG} from "./app/vg/RootVG";
import './resources/resources.scss'
import '@lightningkite/rxjs-plus/index.scss'
import {xStringSubstringBefore, parseObject} from "@lightningkite/khrysalis-runtime";
import {Notifications} from "@lightningkite/rxjs-plus/fcm";

const root = new RootVG()
const v = root.generate(window)
v.classList.add("app")
document.body.appendChild(v)
root.handleDeepLink(window.location.protocol, window.location.host, window.location.pathname, new Map(new URLSearchParams(window.location.search).entries()))

if(window.location.search.indexOf("jwt") !== -1) {
    window.history.replaceState({}, document.title, xStringSubstringBefore(window.location.href, "?"));
}

const messaging = Notifications.INSTANCE.initialize(root, "BFgM2dXhtHnEDMyC1gysnsd_vlanD1O7_eBZ13_WW6M1azZkYMtif4Tp9pM9SFtg2Y6l5ayySKqhkW0TRJ9EXSM", {
    apiKey: "AIzaSyAvRgp2WrIQVzu3PCbLoHw7Nah9SamelUs",
    authDomain: "lightning-kite-template.firebaseapp.com",
    projectId: "lightning-kite-template",
    storageBucket: "lightning-kite-template.appspot.com",
    messagingSenderId: "487565123601",
    appId: "1:487565123601:web:6f0d7e235a89075f5b91b0"
})

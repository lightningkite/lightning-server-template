// [START initialize_firebase_in_sw]
// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here, other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/7.20.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.20.0/firebase-messaging.js');
// Initialize the Firebase app in the service worker by passing in
// your app's Firebase config object.
// https://firebase.google.com/docs/web/setup#config-object
const firebaseConfig = {
    apiKey: "AIzaSyAvRgp2WrIQVzu3PCbLoHw7Nah9SamelUs",
    authDomain: "lightning-kite-template.firebaseapp.com",
    projectId: "lightning-kite-template",
    storageBucket: "lightning-kite-template.appspot.com",
    messagingSenderId: "487565123601",
    appId: "1:487565123601:web:6f0d7e235a89075f5b91b0"
};
firebase.initializeApp(firebaseConfig);
// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();
// [END initialize_firebase_in_sw]

messaging.onBackgroundMessage((message) => {
    console.log(`onBackgroundMessage: ${message}`)
    if (window && window.goToForeground) {
        window.goToForeground()
    }
})
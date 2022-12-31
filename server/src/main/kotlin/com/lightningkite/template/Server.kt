package com.lightningkite.template

import com.lightningkite.lightningdb.*
import com.lightningkite.lightningserver.HtmlDefaults
import com.lightningkite.lightningserver.auth.*
import com.lightningkite.lightningserver.cache.CacheSettings
import com.lightningkite.lightningserver.core.ServerPath
import com.lightningkite.lightningserver.core.ServerPathGroup
import com.lightningkite.lightningserver.db.DatabaseSettings
import com.lightningkite.lightningserver.db.DynamoDbCache
import com.lightningkite.lightningserver.email.EmailSettings
import com.lightningkite.lightningserver.exceptions.NotFoundException
import com.lightningkite.lightningserver.exceptions.SentryExceptionReporter
import com.lightningkite.lightningserver.files.FilesSettings
import com.lightningkite.lightningserver.files.S3FileSystem
import com.lightningkite.lightningserver.files.UploadEarlyEndpoint
import com.lightningkite.lightningserver.http.*
import com.lightningkite.lightningserver.meta.metaEndpoints
import com.lightningkite.lightningserver.notifications.FcmNotificationInterface
import com.lightningkite.lightningserver.notifications.NotificationSettings
import com.lightningkite.lightningserver.serialization.Serialization
import com.lightningkite.lightningserver.settings.generalSettings
import com.lightningkite.lightningserver.settings.setting
import com.lightningkite.lightningserver.tasks.Tasks
import com.lightningkite.lightningserver.tasks.doOnce
import com.lightningkite.lightningserver.tasks.startupOnce
import com.lightningkite.template.stripe.StripeCredentials
import com.lightningkite.template.stripe.StripeEndpoints
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.serialization.serializer
import java.net.URLEncoder
import java.util.*

object Server : ServerPathGroup(ServerPath.root) {

    val files = setting(name = "files", default = FilesSettings())
    val database = setting(name = "database", default = DatabaseSettings())
    val email = setting(name = "email", default = EmailSettings())
    val cache = setting(name = "cache", default = CacheSettings())
    val userSigner = setting(name = "jwt", default = JwtSigner())
    val notifications = setting("notifications", default = NotificationSettings("console"))
    val stripe = setting("stripe", default = StripeCredentials("", ""))

    init {
        MongoDatabase
        DynamoDbCache
        SentryExceptionReporter
        FcmNotificationInterface
        S3FileSystem
        prepareModels()
        Tasks.onSettingsReady {
            files()
        }

        HtmlDefaults.logo = "data:image/svg+xml,%3Csvg id='LK_Logo_Gold_' data-name='LK Logo (Gold)' xmlns='http://www.w3.org/2000/svg' width='225' height='40.099' viewBox='0 0 225 40.099'%3E%3Cpath id='Path_1' data-name='Path 1' d='M137.424,884.183l-5.163,14.345,9.318,1.5-5.786,10.554,12.965-13.669-10.392-1.464,2.661-7.177h15.4v18.722L123.045,924.02,160.8,910.467V884.183Z' transform='translate(-123.045 -883.921)' fill='%23fcb912'%3E%3C/path%3E%3Cg id='Group_1' data-name='Group 1' transform='translate(51.196)'%3E%3Cpath id='Path_2' data-name='Path 2' d='M213.16,910.436V884.184h2.913v23.589h7.469v2.663Z' transform='translate(-213.16 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_3' data-name='Path 3' d='M239.606,884.184v26.252h-2.913V884.184Z' transform='translate(-223.324 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_4' data-name='Path 4' d='M259.828,890.324v2.25h-2.763v-2.4c0-2.363-.934-3.788-3.062-3.788-2.091,0-3.063,1.425-3.063,3.788V904.05c0,2.362.971,3.788,3.063,3.788,2.128,0,3.062-1.425,3.062-3.788V898.8h-2.689v-2.625h5.452V903.9c0,3.975-1.867,6.6-5.9,6.6-4,0-5.863-2.625-5.863-6.6V890.324c0-3.975,1.867-6.6,5.863-6.6C257.962,883.724,259.828,886.349,259.828,890.324Z' transform='translate(-228.235 -883.724)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_5' data-name='Path 5' d='M277.93,898.623v11.813h-2.913V884.184h2.913V896h6.423V884.184h2.913v26.252h-2.913V898.623Z' transform='translate(-239.875 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_6' data-name='Path 6' d='M306.26,910.436V886.847h-4.818v-2.663H313.99v2.663h-4.818v23.589Z' transform='translate(-251.288 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_7' data-name='Path 7' d='M330.007,910.436h-2.614V884.184h3.7l6.05,18.977V884.184h2.577v26.252h-3.025l-6.684-21.227Z' transform='translate(-262.495 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_8' data-name='Path 8' d='M358.638,884.184v26.252h-2.913V884.184Z' transform='translate(-274.732 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_9' data-name='Path 9' d='M370.7,910.436h-2.615V884.184h3.7l6.05,18.977V884.184h2.577v26.252h-3.025L370.7,889.209Z' transform='translate(-280.071 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_10' data-name='Path 10' d='M408.127,890.324v2.25h-2.763v-2.4c0-2.363-.934-3.788-3.063-3.788-2.091,0-3.062,1.425-3.062,3.788V904.05c0,2.362.971,3.788,3.062,3.788,2.129,0,3.063-1.425,3.063-3.788V898.8h-2.69v-2.625h5.453V903.9c0,3.975-1.867,6.6-5.9,6.6-4,0-5.863-2.625-5.863-6.6V890.324c0-3.975,1.867-6.6,5.863-6.6C406.26,883.724,408.127,886.349,408.127,890.324Z' transform='translate(-292.283 -883.724)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_11' data-name='Path 11' d='M440.329,899.035l-1.532,2.587v8.813h-2.913V884.184H438.8V896.86l6.946-12.676h2.95l-6.61,11.963,6.834,14.289h-2.988Z' transform='translate(-309.351 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_12' data-name='Path 12' d='M467.064,884.184v26.252h-2.913V884.184Z' transform='translate(-321.559 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_13' data-name='Path 13' d='M478.96,910.436V886.847h-4.818v-2.663H486.69v2.663h-4.817v23.589Z' transform='translate(-325.874 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3Cpath id='Path_14' data-name='Path 14' d='M509.458,895.809v2.625h-6.386v9.338h7.842v2.663H500.159V884.184h10.756v2.663h-7.842v8.963Z' transform='translate(-337.11 -883.922)' fill='%23fcb912'%3E%3C/path%3E%3C/g%3E%3C/svg%3E"
        HtmlDefaults.basePage = { content ->
            """
            <!doctype html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport"
                      content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
                <meta http-equiv="X-UA-Compatible" content="ie=edge">
                <meta http-equiv="Content-Type" content="text/html charset=UTF-8" />
                <title>${generalSettings().projectName}</title>
                <style>
                    @import url(https://fonts.googleapis.com/css?family=Lato&display=swap);
                    body {
                        font-family: "Lato Light", "serif";
                        background: #f0eae4;
                    }
                    h1, h2 {
                        font-family: "Lato Light", "serif";
                    }
                    .message {
                        position: fixed;
                        left: 50%;
                        top: 40%;
                        text-align: center;
                        transform: translate(-50%, -50%);
                    }
                    .button {
                        background: #62a2b0;
                        border-radius: 4px;
                        color: white;
                        text-transform: uppercase;
                        text-decoration: none;
                        display: block;
                        padding: 8px;
                        margin: 8px;
                    }
                    .button.big {
                        font-size: 24pt;
                    }
                </style>
            </head>
            <body>
            $content
            </body>
            </html>
            """.trimIndent()
        }

    }

    val uploadEarly = UploadEarlyEndpoint(path("upload-early"), files, database, userSigner)

    val index = get.handler {
        val user = it.user<User>()
        val token = userSigner().token(user._id)
        val url = "lightningtemplate://${generalSettings().publicUrl.substringAfter("://").trim('/')}/session?jwt=$token"
        HttpResponse.Companion.html(
            content = HtmlDefaults.basePage(
                """
                <div class="message">
                    ${
                    HtmlDefaults.logo?.let {
                    """
                    <tr><td align="center" style="padding:16px;"><img src="$it" alt="${generalSettings().projectName}"/></td></tr>
                """.trimIndent()
                } ?: ""}
                    <div id="redirect-app">
                        <a class="button" href="$url">Open the App</a>
                    </div>
                    <div id="download-app">
                        <h2>Don't have the app yet?</h2>
                        <a style="display: inline-block; overflow: hidden; border-radius: 13px; width: 250px; height: 83px;">
                            <img src="https://tools.applemediaservices.com/api/badges/download-on-the-app-store/black/en-us?size=250x83&amp;releaseDate=1644796800&h=f7ade19d02e3725b9ccc265d1ae8e5c7"
                                 alt="Download on the App Store" style="border-radius: 13px; width: 250px; height: 83px;">
                        </a>
                        <a style="display: inline-block; overflow: hidden; border-radius: 13px; width: 250px; height: 83px;">
                            <svg width="250px" height="83px" version="1.1" viewBox="0 0 180 53.333" xml:space="preserve"
                                 xmlns="http://www.w3.org/2000/svg">
                                 <g transform="matrix(1.3333 0 0 -1.3333 0 53.333)">
                                     <g transform="scale(.1)"><path d="m1300 0h-1250c-27.5 0-50 22.5-50 50v300c0 27.5 22.5 50 50 50h1250c27.5 0 50-22.5 50-50v-300c0-27.5-22.5-50-50-50" fill="#100f0d"/>
                                         <path
                                                 d="m1300 400h-1250c-27.5 0-50-22.5-50-50v-300c0-27.5 22.5-50 50-50h1250c27.5 0 50 22.5 50 50v300c0 27.5-22.5 50-50 50zm0-7.996c23.16 0 42-18.844 42-42.004v-300c0-23.16-18.84-42.004-42-42.004h-1250c-23.16 0-41.996 18.844-41.996 42.004v300c0 23.16 18.836 42.004 41.996 42.004h1250"
                                                 fill="#a2a2a1"/>
                                         <path
                                                 d="m473.96 302.19h-29.172v-7.219h21.86c-0.594-5.891-2.938-10.516-6.891-13.875-3.953-3.36-9-5.047-14.969-5.047-6.547 0-12.094 2.281-16.64 6.812-4.454 4.625-6.719 10.344-6.719 17.235 0 6.89 2.265 12.609 6.719 17.234 4.546 4.531 10.093 6.797 16.64 6.797 3.36 0 6.563-0.578 9.5-1.844 2.938-1.265 5.297-3.031 7.141-5.297l5.547 5.547c-2.516 2.86-5.703 5.047-9.657 6.641-3.953 1.594-8.078 2.359-12.531 2.359-8.734 0-16.14-3.031-22.187-9.078-6.047-6.062-9.078-13.531-9.078-22.359s3.031-16.313 9.078-22.36c6.047-6.046 13.453-9.078 22.187-9.078 9.172 0 16.485 2.938 22.11 8.907 4.953 4.968 7.484 11.687 7.484 20.093 0 1.422-0.172 2.938-0.422 4.532zm11.301 28v-60.188h35.14v7.391h-27.406v19.093h24.719v7.219h-24.719v19.078h27.406v7.407zm84.418-7.407v7.407h-41.36v-7.407h16.813v-52.781h7.734v52.781zm37.445 7.407h-7.734v-60.188h7.734zm51.051-7.407v7.407h-41.359v-7.407h16.812v-52.781h7.734v52.781zm78.047-0.422c-5.969 6.141-13.281 9.172-22.016 9.172-8.75 0-16.062-3.031-22.031-9.078-5.969-5.969-8.906-13.453-8.906-22.359s2.937-16.391 8.906-22.36c5.969-6.046 13.281-9.078 22.031-9.078 8.656 0 16.047 3.032 22.016 9.078 5.969 5.969 8.906 13.454 8.906 22.36 0 8.828-2.937 16.297-8.906 22.265zm-38.5-5.031c4.453 4.531 9.922 6.797 16.484 6.797 6.547 0 12.016-2.266 16.391-6.797 4.453-4.453 6.64-10.265 6.64-17.234 0-6.985-2.187-12.781-6.64-17.235-4.375-4.531-9.844-6.812-16.391-6.812-6.562 0-12.031 2.281-16.484 6.812-4.36 4.547-6.547 10.25-6.547 17.235 0 6.969 2.187 12.687 6.547 17.234zm65.781-9.844-0.328 11.61h0.328l30.594-49.094h8.078v60.188h-7.734v-35.219l0.328-11.61h-0.328l-29.25 46.829h-9.422v-60.188h7.734z"
                                                 fill="#fff" stroke="#fff" stroke-miterlimit="10" stroke-width="2"/>
                                         <g>
                                             <path d="m1069.4 100h18.66v125.02h-18.66zm168.07 79.98-21.39-54.199h-0.64l-22.2 54.199h-20.1l33.3-75.75-18.99-42.14h19.46l51.31 117.89zm-105.83-65.781c-6.1 0-14.63 3.059-14.63 10.617 0 9.649 10.62 13.348 19.78 13.348 8.2 0 12.07-1.766 17.05-4.18-1.45-11.582-11.42-19.785-22.2-19.785zm2.26 68.516c-13.51 0-27.5-5.953-33.29-19.141l16.56-6.914c3.54 6.914 10.13 9.164 17.05 9.164 9.65 0 19.46-5.785 19.62-16.082v-1.285c-3.38 1.93-10.62 4.824-19.46 4.824-17.86 0-36.03-9.808-36.03-28.144 0-16.727 14.64-27.504 31.04-27.504 12.54 0 19.47 5.6292 23.8 12.226h0.65v-9.656h18.02v47.934c0 22.195-16.58 34.578-37.96 34.578zm-115.32-17.953h-26.54v42.851h26.54c13.95 0 21.87-11.547 21.87-21.425 0-9.688-7.92-21.426-21.87-21.426zm-0.48 60.254h-44.712v-125.02h18.652v47.363h26.06c20.68 0 41.01 14.969 41.01 38.825 0 23.851-20.33 38.828-41.01 38.828zm-243.81-110.84c-12.891 0-23.68 10.797-23.68 25.613 0 14.988 10.789 25.938 23.68 25.938 12.727 0 22.715-10.95 22.715-25.938 0-14.816-9.988-25.613-22.715-25.613zm21.426 58.804h-0.645c-4.187 4.993-12.246 9.504-22.39 9.504-21.27 0-40.762-18.691-40.762-42.695 0-23.84 19.492-42.367 40.762-42.367 10.144 0 18.203 4.5121 22.39 9.6641h0.645v-6.117c0-16.278-8.699-24.973-22.715-24.973-11.434 0-18.523 8.2148-21.426 15.141l-16.269-6.7656c4.668-11.273 17.07-25.133 37.695-25.133 21.914 0 40.441 12.891 40.441 44.309v76.359h-17.726zm30.617-72.98h18.68v125.02h-18.68zm46.231 41.242c-0.481 16.434 12.734 24.809 22.234 24.809 7.414 0 13.691-3.707 15.793-9.02zm57.996 14.18c-3.543 9.5-14.336 27.062-36.407 27.062-21.914 0-40.117-17.238-40.117-42.531 0-23.844 18.047-42.531 42.207-42.531 19.492 0 30.774 11.918 35.449 18.848l-14.5 9.668c-4.835-7.09-11.437-11.762-20.949-11.762-9.5 0-16.269 4.351-20.613 12.89l56.863 23.52zm-453.08 14.012v-18.043h43.175c-1.289-10.149-4.672-17.559-9.828-22.715-6.285-6.281-16.113-13.211-33.347-13.211-26.583 0-47.364 21.426-47.364 48.008s20.781 48.011 47.364 48.011c14.339 0 24.808-5.64 32.542-12.89l12.731 12.73c-10.797 10.309-25.133 18.203-45.273 18.203-36.415 0-67.024-29.644-67.024-66.054 0-36.407 30.609-66.051 67.024-66.051 19.652 0 34.476 6.4451 46.074 18.527 11.922 11.922 15.629 28.676 15.629 42.211 0 4.184-0.325 8.051-0.969 11.274zm110.79-55.258c-12.891 0-24.008 10.633-24.008 25.777 0 15.305 11.117 25.774 24.008 25.774 12.886 0 24.003-10.469 24.003-25.774 0-15.144-11.117-25.777-24.003-25.777zm0 68.308c-23.528 0-42.696-17.882-42.696-42.531 0-24.488 19.168-42.531 42.696-42.531 23.519 0 42.691 18.043 42.691 42.531 0 24.649-19.172 42.531-42.691 42.531zm93.128-68.308c-12.882 0-24.003 10.633-24.003 25.777 0 15.305 11.121 25.774 24.003 25.774 12.891 0 24.004-10.469 24.004-25.774 0-15.144-11.113-25.777-24.004-25.777zm0 68.308c-23.519 0-42.687-17.882-42.687-42.531 0-24.488 19.168-42.531 42.687-42.531 23.528 0 42.696 18.043 42.696 42.531 0 24.649-19.168 42.531-42.696 42.531"
                                                   fill="#fff"/>
                                             <path d="m207.17 205.76-106.46-113c4e-3 -0.0234 0.012-0.043 0.016-0.0664 3.265-12.27 14.472-21.305 27.773-21.305 5.317 0 10.309 1.4375 14.59 3.961l0.34 0.1992 119.84 69.152-56.094 61.059"
                                                   fill="#eb3131"/>
                                             <path d="m314.88 225-0.102 0.07-51.738 29.993-58.289-51.868 58.492-58.484 51.465 29.695c9.023 4.871 15.148 14.383 15.148 25.352 0 10.89-6.043 20.355-14.976 25.242"
                                                   fill="#f6b60b"/>
                                             <path d="m100.7 307.23c-0.64-2.36-0.9764-4.832-0.9764-7.399v-199.68c0-2.5622 0.3324-5.0426 0.9804-7.3942l110.13 110.11-110.13 104.36"
                                                   fill="#5778c5"/>
                                             <path d="m207.96 199.99 55.106 55.09-119.7 69.402c-4.351 2.606-9.429 4.11-14.863 4.11-13.301 0-24.523-9.051-27.789-21.336-4e-3 -0.012-4e-3 -0.02-4e-3 -0.031l107.25-107.24"
                                                   fill="#3bad49"/>
                                         </g>
                                     </g>
                                 </g>
                             </svg>
                        </a>
                    </div>
                </div>
                """.trimIndent()
            )
        )
    }

    val auth = AuthEndpoints(path("auth"))
    val meta = path("meta").metaEndpoints<User> { it.isSuperUser }
    val users = UserEndpoints(path("users"))
    val fcmTokens = FcmTokenEndpoints(path("fcm-tokens"))
    val payment = StripeEndpoints(path("payment"))

    init {
        startupOnce("setUpAdmins", database) {
            val admin = User(
                _id = UUID.fromString("aefe4e8b-4146-437f-a827-03f5954d74f7"),
                email = "joseph@lightningkite.com",
                isSuperUser = true
            )
            if (users.collection().get(admin._id) == null)
                users.collection().upsertOneById(admin._id, admin)
        }
    }
}
package com.lightningkite.template

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.ViewPumpAppCompatDelegate
import com.lightningkite.template.vg.RootVG
import com.lightningkite.androidruntime.SafeInsetsInterceptor
import com.lightningkite.lightningdb.ClientModule
import com.lightningkite.rx.android.SpinnerStyleInterceptor
import com.lightningkite.rx.android.staticApplicationContext
import com.lightningkite.rx.okhttp.HttpClient
import com.lightningkite.rx.okhttp.defaultJsonMapper
import com.lightningkite.rx.viewgenerators.ApplicationAccess
import com.lightningkite.rx.viewgenerators.FocusOnStartupInterceptor
import com.lightningkite.rx.viewgenerators.ViewGenerator
import com.lightningkite.rx.viewgenerators.ViewGeneratorActivity
import com.lightningkite.rx.viewgenerators.fcm.VGFCMService
import com.lightningkite.template.notifications.FCMService
import dev.b3nedikt.viewpump.ViewPump
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.serialization.json.Json

class MainActivity : ViewGeneratorActivity(R.style.AppTheme) {
    companion object {
        val viewData: ViewGenerator by lazy { RootVG() }
    }

    override val main: ViewGenerator
        get() = viewData

    override fun onCreate(savedInstanceState: Bundle?) {
        ViewPump.init(
            SafeInsetsInterceptor,
            SpinnerStyleInterceptor,
            FocusOnStartupInterceptor
        )
        HttpClient.ioScheduler = Schedulers.io()
        HttpClient.responseScheduler = AndroidSchedulers.mainThread()
        staticApplicationContext = applicationContext
        ApplicationAccess.applicationIsActiveStartup(application)
        FCMService.makeChannels(this)
        defaultJsonMapper = Json { serializersModule = ClientModule }
        VGFCMService.main = this.main

        super.onCreate(savedInstanceState)
    }


    private var appCompatDelegate: AppCompatDelegate? = null
    override fun getDelegate(): AppCompatDelegate {
        if (appCompatDelegate == null) {
            appCompatDelegate = ViewPumpAppCompatDelegate(
                super.getDelegate(),
                this
            )
        }
        return appCompatDelegate!!
    }
}

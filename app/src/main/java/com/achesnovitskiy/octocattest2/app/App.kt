package com.achesnovitskiy.octocattest2.app

import androidx.multidex.MultiDexApplication
import com.achesnovitskiy.octocattest2.app.di.AppComponent
import com.achesnovitskiy.octocattest2.app.di.AppModule
import com.achesnovitskiy.octocattest2.app.di.DaggerAppComponent

class App : MultiDexApplication() {

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(
                AppModule(context = this)
            )
            .build()
    }
}
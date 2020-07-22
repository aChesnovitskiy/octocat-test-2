package com.achesnovitskiy.octocattest2.app

import androidx.multidex.MultiDexApplication
import com.achesnovitskiy.octocattest2.app.di.AppComponent
import com.achesnovitskiy.octocattest2.app.di.AppModule
import com.achesnovitskiy.octocattest2.app.di.DaggerAppComponent

class App : MultiDexApplication() {

    companion object {
        lateinit var appComponentInstance: AppComponent

        val appComponent: AppComponent
            get() = appComponentInstance
    }

    override fun onCreate() {
        super.onCreate()

        appComponentInstance = DaggerAppComponent
            .builder()
            .appModule(
                AppModule(context = this)
            )
            .build()
    }
}
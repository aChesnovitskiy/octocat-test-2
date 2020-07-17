package com.achesnovitskiy.octocattest2.app

import androidx.multidex.MultiDexApplication
import com.achesnovitskiy.octocattest2.app.di.AppComponent
import com.achesnovitskiy.octocattest2.app.di.DaggerAppComponent

class App : MultiDexApplication() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .build()
    }
}
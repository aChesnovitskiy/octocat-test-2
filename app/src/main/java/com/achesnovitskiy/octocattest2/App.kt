package com.achesnovitskiy.octocattest2

import android.app.Application
import com.achesnovitskiy.octocattest2.di.AppComponent
import com.achesnovitskiy.octocattest2.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.create()
    }
}
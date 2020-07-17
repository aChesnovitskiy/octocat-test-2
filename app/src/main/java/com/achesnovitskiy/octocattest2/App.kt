package com.achesnovitskiy.octocattest2

import androidx.multidex.MultiDexApplication
import com.achesnovitskiy.octocattest2.di.AppComponent
import com.achesnovitskiy.octocattest2.di.DaggerAppComponent

class App : MultiDexApplication() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .build()
    }
}
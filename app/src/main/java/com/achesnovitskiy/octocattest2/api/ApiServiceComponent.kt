package com.achesnovitskiy.octocattest2.api

import dagger.Subcomponent


@Subcomponent(modules = [])
interface ApiServiceComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): ApiServiceComponent
    }
}
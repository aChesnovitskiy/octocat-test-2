package com.achesnovitskiy.octocattest2.di

import com.achesnovitskiy.octocattest2.api.di.ApiServiceComponent
import com.achesnovitskiy.octocattest2.repoinfo.di.RepoInfoComponent
import dagger.Module

@Module(subcomponents = [RepoInfoComponent::class])
class AppSubcomponents
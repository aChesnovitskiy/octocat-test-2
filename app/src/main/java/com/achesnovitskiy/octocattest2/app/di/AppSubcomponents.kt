package com.achesnovitskiy.octocattest2.app.di

import com.achesnovitskiy.octocattest2.repoinfo.di.RepoInfoComponent
import com.achesnovitskiy.octocattest2.repos.di.ReposComponent
import dagger.Module

@Module(subcomponents = [ReposComponent::class, RepoInfoComponent::class])
class AppSubcomponents
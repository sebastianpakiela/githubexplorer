package com.sebastianpakiela.githubexplorer.di

import com.sebastianpakiela.githubexplorer.MainActivity
import dagger.Module

import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}
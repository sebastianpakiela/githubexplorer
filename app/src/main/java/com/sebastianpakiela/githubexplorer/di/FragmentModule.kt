package com.sebastianpakiela.githubexplorer.di

import com.sebastianpakiela.githubexplorer.feature.details.DetailsFragment
import com.sebastianpakiela.githubexplorer.feature.search.SearchFragment
import dagger.Module

import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): DetailsFragment
}
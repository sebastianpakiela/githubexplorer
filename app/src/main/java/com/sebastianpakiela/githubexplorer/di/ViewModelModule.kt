package com.sebastianpakiela.githubexplorer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sebastianpakiela.githubexplorer.feature.details.DetailsViewModel
import com.sebastianpakiela.githubexplorer.feature.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun searchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    internal abstract fun detailsViewModel(viewModel: DetailsViewModel): ViewModel
}
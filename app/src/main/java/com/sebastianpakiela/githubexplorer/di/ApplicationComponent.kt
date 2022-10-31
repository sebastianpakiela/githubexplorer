package com.sebastianpakiela.githubexplorer.di

import android.app.Application
import com.sebastianpakiela.githubexplorer.GithubExplorerApp
import com.sebastianpakiela.githubexplorer.data.di.DataModule
import com.sebastianpakiela.githubexplorer.domain.di.DomainModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        DomainModule::class,
        DataModule::class,
        AppModule::class
    ]
)
@Singleton
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent

        @BindsInstance
        fun application(application: Application): Builder
    }

    fun inject(app: GithubExplorerApp)
}
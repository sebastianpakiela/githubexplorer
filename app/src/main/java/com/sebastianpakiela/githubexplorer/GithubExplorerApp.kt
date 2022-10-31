package com.sebastianpakiela.githubexplorer

import android.app.Application
import com.sebastianpakiela.githubexplorer.di.ApplicationComponent
import com.sebastianpakiela.githubexplorer.di.DaggerApplicationComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class GithubExplorerApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = activityInjector

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent
            .builder()
            .application(this)
            .build()

        component.inject(this)
    }

}
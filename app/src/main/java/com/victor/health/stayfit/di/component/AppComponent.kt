package com.victor.health.stayfit.di.component

import android.app.Application
import com.victor.health.stayfit.di.modules.*
import dagger.Component
import javax.inject.Singleton

/**
 * Created by victorpalmacarrasco on 14/9/18.
 * ${APP_NAME}
 */

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataManagerModule::class, DataBaseModule::class])
interface AppComponent {
    fun plus(presenterModule: PresenterModule): PresenterComponent
    fun inject(application: Application)
}
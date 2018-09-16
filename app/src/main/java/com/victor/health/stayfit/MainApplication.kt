package com.victor.health.stayfit

import android.app.Application
import com.victor.health.stayfit.di.component.AppComponent
import com.victor.health.stayfit.di.component.DaggerAppComponent
import com.victor.health.stayfit.di.component.PresenterComponent
import com.victor.health.stayfit.di.modules.AppModule
import com.victor.health.stayfit.di.modules.PresenterModule

/**
 * Created by victorpalmacarrasco on 14/9/18.
 * ${APP_NAME}
 */
class MainApplication: Application() {

    private val appComponent: AppComponent by lazy { DaggerAppComponent.builder().appModule(AppModule(this)).build() }
    var presenterComponent: PresenterComponent? = null


    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }



    fun createPresenterComponent(): PresenterComponent {
        return appComponent.plus(PresenterModule())
    }

    fun releasePresenterComponent() {
        presenterComponent = null
    }
}
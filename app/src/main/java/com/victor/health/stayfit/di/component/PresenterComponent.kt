package com.victor.health.stayfit.di.component

import com.victor.health.stayfit.ui.main.MainActivity
import com.victor.health.stayfit.di.modules.PresenterModule
import com.victor.health.stayfit.di.scopes.ViewScope
import dagger.Subcomponent

/**
 * Created by victorpalmacarrasco on 14/9/18.
 * ${APP_NAME}
 */

@ViewScope
@Subcomponent(modules = [PresenterModule::class])
interface PresenterComponent {
    fun inject(target: MainActivity)
}
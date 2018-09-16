package com.victor.health.stayfit.di.component

import com.victor.health.stayfit.di.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by victorpalmacarrasco on 13/9/18.
 * ${APP_NAME}
 */
@Singleton
@Component(modules = [NetworkModule::class])
interface NetworkComponent
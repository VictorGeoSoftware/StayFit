package com.victor.health.stayfit.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.victor.health.stayfit.data.databases.GoalDataBase
import com.victor.health.stayfit.data.mappers.GoalMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by victorpalmacarrasco on 14/9/18.
 * ${APP_NAME}
 */

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application
}
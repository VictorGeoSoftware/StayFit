package com.victor.health.stayfit.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.victor.health.stayfit.data.databases.GoalDataBase
import com.victor.health.stayfit.data.mappers.GoalMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */

@Module
open class DataBaseModule {

    @Provides
    @Singleton
    open fun provideGoalDataBase(context: Context): GoalDataBase
            = Room.databaseBuilder(context, GoalDataBase::class.java, "goal.db").build()

    @Provides
    @Singleton
    open fun provideGoalMapper(): GoalMapper = GoalMapper()
}
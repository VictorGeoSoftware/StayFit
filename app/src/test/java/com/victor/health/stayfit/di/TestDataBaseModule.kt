package com.victor.health.stayfit.di

import android.content.Context
import com.victor.health.stayfit.data.databases.GoalDataBase
import com.victor.health.stayfit.di.modules.DataBaseModule

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */
class TestDataBaseModule(private val context: Context): DataBaseModule() {

    override fun provideGoalDataBase(context: Context): GoalDataBase {
        return super.provideGoalDataBase(this.context)
    }
}
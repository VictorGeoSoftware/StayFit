package com.victor.health.stayfit.di.modules

import android.content.Context
import com.victor.health.stayfit.data.DataManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by victorpalmacarrasco on 14/9/18.
 * ${APP_NAME}
 */

@Module
class DataManagerModule {

    @Provides
    @Singleton
    fun provideDataManager(context: Context): DataManager = DataManager(context)
}
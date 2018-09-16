package com.victor.health.stayfit.di.modules

import com.victor.health.stayfit.data.DataManager
import com.victor.health.stayfit.data.databases.GoalDataBase
import com.victor.health.stayfit.data.mappers.GoalMapper
import com.victor.health.stayfit.di.scopes.ViewScope
import com.victor.health.stayfit.network.GoalsRepository
import com.victor.health.stayfit.presenter.main.MainPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

/**
 * Created by victorpalmacarrasco on 14/9/18.
 * ${APP_NAME}
 */

@Module
class PresenterModule {

    companion object {
        const val ANDROID_SCHEDULER = "ANDROID_SCHEDULER"
        const val TASK_SCHEDULER = "TASK_SCHEDULER"
    }



    @Provides
    @Named(ANDROID_SCHEDULER)
    fun provideAndroidScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Named(TASK_SCHEDULER)
    fun provideTaskScheduler(): Scheduler = Schedulers.newThread()

    @Provides
    @ViewScope
    fun provideMainPresenter(
            @Named(ANDROID_SCHEDULER) androidScheduler: Scheduler,
            @Named(TASK_SCHEDULER) taskScheduler: Scheduler,
            goalsRepository: GoalsRepository,
            goalDataBase: GoalDataBase,
            goalMapper: GoalMapper) = MainPresenter(androidScheduler, taskScheduler, goalsRepository, goalDataBase, goalMapper)
}
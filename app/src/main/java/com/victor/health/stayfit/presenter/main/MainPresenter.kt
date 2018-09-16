package com.victor.health.stayfit.presenter.main

import com.victor.health.stayfit.data.databases.Goal
import com.victor.health.stayfit.data.databases.GoalDataBase
import com.victor.health.stayfit.data.mappers.GoalMapper
import com.victor.health.stayfit.data.models.GoalItemDto
import com.victor.health.stayfit.network.GoalsRepository
import com.victor.health.stayfit.presenter.ParentPresenter
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by victorpalmacarrasco on 13/9/18.
 * ${APP_NAME}
 */


class MainPresenter @Inject constructor(
        private val androidScheduler: Scheduler,
        private val subscriberScheduler: Scheduler,
        private val goalsRepository: GoalsRepository,
        private val goalDataBase: GoalDataBase,
        private val goalMapper: GoalMapper): ParentPresenter<MainPresenter.MainView>() {


    interface MainView {
        fun onGoalListReceived(goalList: ArrayList<GoalItemDto>) { }
        fun onGoalListError(throwable: Throwable) { }
    }


    private val compositeDisposable = CompositeDisposable()


    fun getGoalList() {
        goalsRepository.getGoals()
                .observeOn(androidScheduler)
                .subscribeOn(subscriberScheduler)
                .subscribe(
                        {
                            view?.onGoalListReceived(it.items)
                            val goalList = goalMapper.mapArray(it.items)
                            goalDataBase.goalDao().instertAllGoals(goalList)
                        }, {
                            view?.onGoalListError(it)
                        })
    }

    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }
}
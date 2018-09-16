package com.victor.health.stayfit.presenter.main

import com.victor.health.stayfit.data.databases.Goal
import com.victor.health.stayfit.data.databases.GoalDataBase
import com.victor.health.stayfit.data.mappers.GoalMapper
import com.victor.health.stayfit.network.GoalsRepository
import com.victor.health.stayfit.presenter.ParentPresenter
import io.reactivex.Observable
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
        fun onGoalListReceived(goalList: List<Goal>) { }
        fun onGoalListError(throwable: Throwable) { }
    }


    private val compositeDisposable = CompositeDisposable()


    fun getGoalList() {
        goalsRepository.getGoals()
                .flatMap {
                    goalDataBase.goalDao().clearAllGoals()
                    val goalList = goalMapper.mapArray(it.items)
                    goalDataBase.goalDao().insertAllGoals(goalList)
                    Observable.just(goalList)
                }
                .observeOn(androidScheduler)
                .subscribeOn(subscriberScheduler)
                .subscribe(
                        {
                            view?.onGoalListReceived(it)
                        }, {
                            view?.onGoalListError(it)
                        })
    }

    fun getPersistedGoalList() {
        goalDataBase.goalDao().getAllGoals()
                .observeOn(androidScheduler)
                .subscribeOn(subscriberScheduler)
                .subscribe {
                    view?.onGoalListReceived(it)
                }
    }

    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }
}
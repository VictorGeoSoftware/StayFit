package com.victor.health.stayfit.data.databases

import android.arch.persistence.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */

@Dao
interface GoalDao {

    @Insert
    fun insertAllGoals(goals: ArrayList<Goal>)

    @Update
    fun update(goal: Goal)

    @Delete
    fun delete(goal: Goal)

    @Query("SELECT * FROM goals")
    fun getAllGoals(): Flowable<List<Goal>>

    @Query("DELETE FROM goals")
    fun clearAllGoals()
}
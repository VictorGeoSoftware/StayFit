package com.victor.health.stayfit.data.databases

import android.arch.persistence.room.*

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */

@Dao
interface GoalDao {

    @Insert
    fun instertAllGoals(goals: ArrayList<Goal>)

    @Update
    fun update(goal: Goal)

    @Delete
    fun delete(goal: Goal)

    @Query("SELECT * FROM goals")
    fun getAllGoals(): List<Goal>
}
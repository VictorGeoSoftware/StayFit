package com.victor.health.stayfit.data.databases

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */

// todo:: https://developer.android.com/training/data-storage/room/testing-db

@Database(entities = [Goal::class], version = 1)
abstract class GoalDataBase: RoomDatabase() {

    abstract fun goalDao(): GoalDao
}
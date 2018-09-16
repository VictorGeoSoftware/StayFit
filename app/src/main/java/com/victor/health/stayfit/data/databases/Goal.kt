package com.victor.health.stayfit.data.databases

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.victor.health.stayfit.data.models.RewardDto

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */


@Entity(tableName = "goals")
data class Goal (
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "type") val type: String,
        @ColumnInfo(name = "goal") val goal: Long,
        @Embedded val reward: RewardDto)
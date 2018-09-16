package com.victor.health.stayfit.data.models

/**
 * Created by victorpalmacarrasco on 13/9/18.
 * ${APP_NAME}
 */

data class GoalItemDto(val id: Long, val title: String, val description: String, val type: String,
                       val goal: Long, val reward: RewardDto)
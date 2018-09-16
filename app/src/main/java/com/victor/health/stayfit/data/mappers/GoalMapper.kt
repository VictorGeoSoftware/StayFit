package com.victor.health.stayfit.data.mappers

import com.victor.health.stayfit.data.databases.Goal
import com.victor.health.stayfit.data.models.GoalItemDto

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */
class GoalMapper {
    fun map (goalItemDto: GoalItemDto): Goal {
        return Goal(goalItemDto.id, goalItemDto.title, goalItemDto.description, goalItemDto.type, goalItemDto.goal, goalItemDto.reward)
    }

    fun mapArray (goalItemDtoList: ArrayList<GoalItemDto>): ArrayList<Goal> {
        val goalList = ArrayList<Goal>()
        for (goalItem in goalItemDtoList) {
            goalList.add(map(goalItem))
        }

        return goalList
    }
}
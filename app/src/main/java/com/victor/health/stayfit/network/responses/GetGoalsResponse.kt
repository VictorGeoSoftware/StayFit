package com.victor.health.stayfit.network.responses

import com.victor.health.stayfit.data.models.GoalItemDto

/**
 * Created by victorpalmacarrasco on 13/9/18.
 * ${APP_NAME}
 */
data class GetGoalsResponse(val items: ArrayList<GoalItemDto>, val nextPageToken: String)
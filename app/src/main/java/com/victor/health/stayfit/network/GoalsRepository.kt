package com.victor.health.stayfit.network

import com.victor.health.stayfit.network.responses.GetGoalsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Created by victorpalmacarrasco on 13/9/18.
 * ${APP_NAME}
 */
interface GoalsRepository {
    @Headers("Content-Type: application/json;charset=UTF-8")

    @GET("_ah/api/myApi/v1/goals")
    fun getGoals(): Observable<GetGoalsResponse>
}
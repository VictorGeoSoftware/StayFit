package com.victor.health.stayfit.di

import com.victor.health.stayfit.di.modules.NetworkModule
import com.victor.health.stayfit.network.GoalsRepository
import org.mockito.Mockito
import retrofit2.Retrofit

/**
 * Created by victorpalmacarrasco on 14/9/18.
 * ${APP_NAME}
 */
class TestNetworkModule: NetworkModule() {

    override fun provideGetGoalsRequest(retrofit: Retrofit): GoalsRepository {
//        return super.provideGetGoalsRequest(retrofit)
        return Mockito.mock(GoalsRepository::class.java)
    }
}
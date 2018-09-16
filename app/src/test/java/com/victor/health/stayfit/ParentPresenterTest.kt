package com.victor.health.stayfit

import com.victor.health.stayfit.di.TestNetworkModule
import com.victor.health.stayfit.di.component.NetworkComponent
import com.victor.health.stayfit.di.modules.NetworkModule
import dagger.Component
import org.junit.Before
import javax.inject.Singleton

/**
 * Created by victorpalmacarrasco on 13/9/18.
 * ${APP_NAME}
 */
open class ParentPresenterTest {
    open lateinit var testNetworkComponent: TestNetworkComponent


    @Singleton
    @Component(modules = [NetworkModule::class])
    interface TestNetworkComponent: NetworkComponent {
        fun inject(target: MainPresenterTest)
    }


    @Before
    open fun setUp() {
        testNetworkComponent = DaggerParentPresenterTest_TestNetworkComponent.builder()
                .networkModule(TestNetworkModule())
                .build()
    }

}
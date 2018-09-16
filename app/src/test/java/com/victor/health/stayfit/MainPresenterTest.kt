package com.victor.health.stayfit

import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import com.victor.health.stayfit.data.DataManager
import com.victor.health.stayfit.data.databases.GoalDataBase
import com.victor.health.stayfit.data.mappers.GoalMapper
import com.victor.health.stayfit.network.GoalsRepository
import com.victor.health.stayfit.network.responses.GetGoalsResponse
import com.victor.health.stayfit.presenter.main.MainPresenter
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject

/**
 * Created by victorpalmacarrasco on 13/9/18.
 * ${APP_NAME}
 */

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest: ParentPresenterTest() {

    @Inject lateinit var goalsRepository: GoalsRepository

    @Mock lateinit var goalDataBase: GoalDataBase
    private var goalMapper = GoalMapper()

    @Mock lateinit var mockView: MainPresenter.MainView
    @Mock lateinit var dataManager: DataManager
    private lateinit var testScheduler: TestScheduler
    private lateinit var mainPresenter: MainPresenter


    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.initMocks(this)
        testNetworkComponent.inject(this)
        mainPresenter = createMockedMainPresenter()
    }

    private fun createMockedMainPresenter(): MainPresenter {
        testScheduler = TestScheduler()
        val mainPresenter = MainPresenter(testScheduler, testScheduler, goalsRepository, goalDataBase, goalMapper)
        mainPresenter.view = mockView
        return mainPresenter
    }

    private fun createMockedResponse(): GetGoalsResponse {
        val mockedResponseRaw = "{\n" +
                " \"items\": [\n" +
                "  {\n" +
                "   \"id\": \"1000\",\n" +
                "   \"title\": \"Easy walk steps\",\n" +
                "   \"description\": \"Walk 500 steps a day\",\n" +
                "   \"type\": \"step\",\n" +
                "   \"goal\": 500,\n" +
                "   \"reward\": {\n" +
                "    \"trophy\": \"bronze_medal\",\n" +
                "    \"points\": 5\n" +
                "   }\n" +
                "  }\n" +
                " ],\n" +
                " \"nextPageToken\": \"not used\"\n" +
                "}"

        val gson = Gson()
        return gson.fromJson(mockedResponseRaw, GetGoalsResponse::class.java)
    }



    @Test
    fun `should perform a call and retrieve some response`() {
        val mockedResponse = createMockedResponse()
        whenever(goalsRepository.getGoals()).thenReturn(Observable.just(mockedResponse))

        mainPresenter.getGoalList()
        testScheduler.triggerActions()


        verify(mockView, times(1)).onGoalListReceived(goalMapper.mapArray(mockedResponse.items))
    }

    @Test
    fun `should perform a call and retrieve an error`() {
        val throwable = Throwable()
        val error = Observable.error<GetGoalsResponse>(throwable)
        whenever(goalsRepository.getGoals()).thenReturn(error)

        mainPresenter.getGoalList()
        testScheduler.triggerActions()

        verify(mockView, times(1)).onGoalListError(throwable)
    }
}
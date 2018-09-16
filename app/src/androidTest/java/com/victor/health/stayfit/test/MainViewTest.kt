package com.victor.health.stayfit.test

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.uiautomator.UiDevice
import com.victor.health.stayfit.R
import com.victor.health.stayfit.assertions.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.victor.health.stayfit.ui.main.MainActivity
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import junit.framework.Assert.assertNotNull
import org.hamcrest.Matchers.greaterThan
import org.junit.Rule

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */
class MainViewTest {
    @Rule
    val mainActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    private lateinit var mDevice: UiDevice
    private lateinit var mainActivity: MainActivity


    @Before
    fun setUp() {
        Intents.init()
        mainActivityTestRule.launchActivity(Intent())
        mainActivity = mainActivityTestRule.activity
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @After
    fun tearDown() {
        Intents.release()
        mainActivityTestRule.finishActivity()
    }



    @Given("^I am in the main view screen")
    fun i_am_in_the_main_view_screen() {
        assertNotNull(mainActivity)
    }

    @When("^I wait for a moment")
    fun i_wait_for_a_moment() {
        Thread.sleep(1000)
    }

    @Then("^I see all the goals in a list")
    fun i_see_all_the_goals_in_a_list() {
        onView(withId(R.id.lstGoals)).check(withItemCount(greaterThan(0)))
    }
}
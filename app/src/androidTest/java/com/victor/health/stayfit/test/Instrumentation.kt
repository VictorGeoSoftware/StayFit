package com.victor.health.stayfit.test

import android.os.Bundle
import android.support.test.runner.MonitoringInstrumentation
import cucumber.api.CucumberOptions
import cucumber.api.android.CucumberInstrumentationCore

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */

@CucumberOptions(features = ["features/main_view_test.feature"], glue = ["com.victor.health.stayfit.test"])
class Instrumentation: MonitoringInstrumentation() {
    private val instrumentationCore = CucumberInstrumentationCore(this)


    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        instrumentationCore.create(arguments)
        start()
    }

    override fun onStart() {
        super.onStart()
        waitForIdleSync()
        instrumentationCore.start()
    }
}
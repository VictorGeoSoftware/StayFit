package com.victor.health.stayfit.ui.running

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessActivities
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.fitness.result.DailyTotalResult
import com.victor.health.stayfit.R
import com.victor.health.stayfit.data.Constants.Companion.DISTANCE
import com.victor.health.stayfit.data.Constants.Companion.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE
import com.victor.health.stayfit.data.Constants.Companion.STAY_FIT_SESSION
import com.victor.health.stayfit.data.Constants.Companion.STEPS
import com.victor.health.stayfit.ui.ParentActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_running.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by victorpalmacarrasco on 16/9/18.
 * ${APP_NAME}
 */
class RunningActivity: ParentActivity() {

    lateinit var fitnessOptions: FitnessOptions
    lateinit var sessionStartTime: Date
    private var session: Session? = null
    private var fitApiClient: GoogleApiClient? = null
    private var training = false
    private var mStepsListener: OnDataPointListener? = null
    private var mDistanceListener: OnDataPointListener? = null
    private var totalSteps = 0
    private var totalDistance: Float = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        sessionStartTime = Date()

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_LOCATION_SAMPLE, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_WRITE)
                .build()

        val scopeLocation = Scope(Scopes.FITNESS_LOCATION_READ_WRITE)
        val scopeActivity = Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE)

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    scopeActivity,
                    scopeLocation)
        } else {
            accessGoogleFit()
        }

        btnSaveProgress.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        uploadRegisteredActivity()
        unregisterListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            accessGoogleFit()
        }
    }

    override fun onClick(view: View?) {
        if (view == btnSaveProgress) {
            uploadRegisteredActivity()
        }
    }



    private fun accessGoogleFit() {
        fitApiClient = GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SESSIONS_API)
                .addScope(Scope((Scopes.FITNESS_LOCATION_READ_WRITE)))
                .addScope(Scope((Scopes.FITNESS_ACTIVITY_READ_WRITE)))
                .addOnConnectionFailedListener {
                    Log.i("RunningActivity", "accessGoogleFit - addOnConnectionFailedListener $it")
                }
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(p0: Bundle?) {
                        printDailyStepsCountInUI()
                        findFitnessDataSources()
                    }

                    override fun onConnectionSuspended(p0: Int) {
                        Log.i("RunningActivity", "accessGoogleFit - onConnectionSuspended!")
                    }
                })
                .build()

        fitApiClient?.connect()
    }

    private fun printDailyStepsCountInUI() {
        getDailyStepsCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            var steps = 0
                            val totalSet = it.total

                            totalSet.let {
                                if (!it!!.isEmpty) {
                                     steps = it.dataPoints[0].getValue(Field.FIELD_STEPS).asInt()
                                }
                            }

                            txtTotalStepsCount.text = String.format(getString(R.string.total_steps_count), steps.toString())
                        },
                        {
                            Log.i("RunningActivity", "printDailyStepsCountInUI - error :: $it")
                        })
    }

    private fun getDailyStepsCount(): Observable<DailyTotalResult> {
        return Observable.create {
            val result = Fitness.HistoryApi.readDailyTotal(fitApiClient, DataType.TYPE_STEP_COUNT_DELTA)
                    .await(30, TimeUnit.SECONDS)

            if (result.status.isSuccess) {
                it.onNext(result)
                it.onComplete()
            } else {
                it.onError(Throwable(result.status.statusMessage))
            }
        }
    }

    private fun findFitnessDataSources() {
        Fitness.SensorsApi.findDataSources(fitApiClient, DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_DISTANCE_DELTA, DataType.TYPE_STEP_COUNT_DELTA)
                .build())
                .setResultCallback {
                    for (dataSource in it.dataSources) {
                        Log.i("RunningActivity", "findFitnessDataSources - ${dataSource.dataType}!")
                        if (dataSource.dataType == DataType.TYPE_STEP_COUNT_DELTA) {
                            registerStepListener(dataSource, dataSource.dataType)
                        } else if (dataSource.dataType == DataType.TYPE_DISTANCE_DELTA) {
                            registerDistanceListener(dataSource, DataType.TYPE_DISTANCE_DELTA)
                        }
                    }
                }
    }

    private fun registerStepListener(dataSource: DataSource, dataType: DataType) {
        mStepsListener = OnDataPointListener {
            for (field in it.dataType.fields) {
                if (field?.name.equals(STEPS)) {
                    val value = it.getValue(field).asInt()
                    totalSteps += value
                    txtCurrentSteps.text = String.format(getString(R.string.current_steps_count, totalSteps.toString()))
                }
            }
        }

        Fitness.SensorsApi.add(
                fitApiClient,
                SensorRequest.Builder()
                .setDataSource(dataSource)
                .setDataType(dataType)
                .setSamplingRate(1, TimeUnit.SECONDS)
                .build(),
                mStepsListener)
                .setResultCallback {
//                    if (it.isSuccess) {
//                        Toast.makeText(this, getString(R.string.ready_for_traking), Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, getString(R.string.problems_opening_session), Toast.LENGTH_SHORT).show()
//                    }
                }
    }

    private fun registerDistanceListener(dataSource: DataSource, dataType: DataType) {
        mDistanceListener = OnDataPointListener {
            for (field in it.dataType.fields) {
                if (field?.name.equals(DISTANCE)) {
                    val value = it.getValue(field)
                    totalDistance += value.asFloat()
                    txtDistance.text = String.format(getString(R.string.current_distance), totalDistance.toString())
                }
            }
        }

        Fitness.SensorsApi.add(
                fitApiClient,
                SensorRequest.Builder()
                .setDataSource(dataSource)
                .setDataType(dataType)
                .setSamplingRate(1, TimeUnit.SECONDS)
                .build(),
                mDistanceListener)
                .setResultCallback {
//                    if (it.isSuccess) {
//                        Toast.makeText(this, getString(R.string.ready_for_traking), Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, getString(R.string.problems_opening_session), Toast.LENGTH_SHORT).show()
//                    }
                }
    }

    private fun unregisterListener(){
        mStepsListener?.let {
            Fitness.SensorsApi.remove(fitApiClient, mStepsListener)
                    .setResultCallback {
                        if (it.isSuccess) {
                            Toast.makeText(this, getString(R.string.session_finished), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, getString(R.string.problems_closing_google_fit_session), Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    private fun uploadRegisteredActivity() {
        val rightNow = Date()

        session = Session.Builder()
                .setName(STAY_FIT_SESSION)
                .setIdentifier(getString(R.string.app_name) + " " + System.currentTimeMillis())
                .setDescription(getString(R.string.details))
                .setStartTime(sessionStartTime.time, TimeUnit.MILLISECONDS)
                .setEndTime(rightNow.time, TimeUnit.MILLISECONDS)
                .setActivity(FitnessActivities.RUNNING)
                .build()

        val stepsDataSource = DataSource.Builder()
                .setAppPackageName(this.packageName)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setStreamName(getString(R.string.total_steps_measured))
                .setType(DataSource.TYPE_RAW)
                .build()

        val stepsDataSet = DataSet.create(stepsDataSource)

        val stepsDataPoint = stepsDataSet.createDataPoint().setTimeInterval(sessionStartTime.time, rightNow.time, TimeUnit.MILLISECONDS)
        stepsDataPoint.getValue(Field.FIELD_STEPS).setInt(totalSteps)
        stepsDataSet.add(stepsDataPoint)

        GoogleSignIn.getLastSignedInAccount(this).let {
            Fitness.getHistoryClient(this, it!!).insertData(stepsDataSet)
        }
    }
}
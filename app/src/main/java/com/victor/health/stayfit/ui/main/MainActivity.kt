package com.victor.health.stayfit.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.victor.health.stayfit.R
import com.victor.health.stayfit.data.Constants.Companion.LOCATION_PERMISSION_REQUEST
import com.victor.health.stayfit.data.Constants.Companion.RUNNING_DISTANCE
import com.victor.health.stayfit.data.databases.Goal
import com.victor.health.stayfit.presenter.main.MainPresenter
import com.victor.health.stayfit.ui.ParentActivity
import com.victor.health.stayfit.ui.SpaceDecorator
import com.victor.health.stayfit.ui.running.RunningActivity
import com.victor.health.stayfit.utils.getDpFromValue
import com.victor.health.stayfit.utils.mainApplication
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : ParentActivity(), MainPresenter.MainView, GoalListAdapter.GoalSelectedListener {
    companion object {
        const val ROW_SPACE = 10
    }

    @Inject lateinit var mainPresenter: MainPresenter
    private lateinit var goalListAdapter: GoalListAdapter
    private val mGoalList = ArrayList<Goal>()
    private var mSelectedGoalBeforePermission:Goal? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainApplication.createPresenterComponent().inject(this)

        lstGoals.layoutManager = LinearLayoutManager(this)
        lstGoals.addItemDecoration(SpaceDecorator(getDpFromValue(ROW_SPACE)))

        goalListAdapter = GoalListAdapter(mGoalList, this)
        lstGoals.adapter = goalListAdapter

        mainPresenter.view = this
        mainPresenter.getGoalList()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.destroy()
        mainApplication.releasePresenterComponent()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mSelectedGoalBeforePermission.let {
                if (it!!.type.contentEquals(RUNNING_DISTANCE)) {
                    val intent = Intent(this, RunningActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }



    override fun onGoalListReceived(goalList: List<Goal>) {
        mGoalList.clear()
        mGoalList.addAll(goalList)
        goalListAdapter.notifyDataSetChanged()
    }

    override fun onGoalListError(throwable: Throwable) {
        Toast.makeText(this, getString(R.string.msg_error_conection), Toast.LENGTH_SHORT).show()
        mainPresenter.getPersistedGoalList()
    }

    override fun onGoalSelected(goal: Goal) {
        if (goal.type.contentEquals(RUNNING_DISTANCE)) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, RunningActivity::class.java)
                startActivity(intent)
            } else {
                mSelectedGoalBeforePermission = goal
                val permission = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST)
            }
        }


    }
}

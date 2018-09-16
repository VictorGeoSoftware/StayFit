package com.victor.health.stayfit.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.victor.health.stayfit.R
import com.victor.health.stayfit.data.databases.GoalDataBase
import com.victor.health.stayfit.data.models.GoalItemDto
import com.victor.health.stayfit.presenter.main.MainPresenter
import com.victor.health.stayfit.ui.SpaceDecorator
import com.victor.health.stayfit.utils.getDpFromValue
import com.victor.health.stayfit.utils.mainApplication
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainPresenter.MainView, GoalListAdapter.GoalSelectedListener {
    companion object {
        const val ROW_SPACE = 10
    }

    @Inject lateinit var mainPresenter: MainPresenter
    private lateinit var goalListAdapter: GoalListAdapter
    private val mGoalList = ArrayList<GoalItemDto>()


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


    override fun onGoalListReceived(goalList: ArrayList<GoalItemDto>) {
        Log.i("MainActivity", "onGoalListReceived :: ${goalList.size}")
        mGoalList.clear()
        mGoalList.addAll(goalList)
        goalListAdapter.notifyDataSetChanged()
    }

    override fun onGoalListError(throwable: Throwable) {
        Log.i("MainActivity", "onGoalListError :: ${throwable.message}")
    }

    override fun onGoalSelected(goalItemDto: GoalItemDto) {

    }
}

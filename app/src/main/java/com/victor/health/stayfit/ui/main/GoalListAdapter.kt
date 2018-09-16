package com.victor.health.stayfit.ui.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.victor.health.stayfit.R
import com.victor.health.stayfit.data.databases.Goal
import com.victor.health.stayfit.utils.inflate
import kotlinx.android.synthetic.main.goal_list_adapter_row.view.*

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */
class GoalListAdapter(private val goalList: ArrayList<Goal>, private val goalSelectedListener: GoalSelectedListener):
        RecyclerView.Adapter<GoalListAdapter.GoalViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder(parent.inflate(R.layout.goal_list_adapter_row))
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(goalList[position], goalSelectedListener)
    }


    class GoalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(goal: Goal, goalSelectedListener: GoalSelectedListener) = with(itemView) {
            val rewardContent = String.format(itemView.context.getString(R.string.reward_text),
                    goal.reward.trophy,
                    goal.reward.points.toString())

            title.text = goal.title
            description.text = goal.description
            reward.text = rewardContent


            this.setOnClickListener {
                goalSelectedListener.onGoalSelected(goal)
            }
        }
    }

    interface GoalSelectedListener {
        fun onGoalSelected(goal: Goal)
    }
}
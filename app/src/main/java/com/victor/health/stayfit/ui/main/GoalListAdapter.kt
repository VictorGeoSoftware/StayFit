package com.victor.health.stayfit.ui.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.victor.health.stayfit.R
import com.victor.health.stayfit.data.models.GoalItemDto
import com.victor.health.stayfit.utils.inflate
import kotlinx.android.synthetic.main.goal_list_adapter_row.view.*

/**
 * Created by victorpalmacarrasco on 15/9/18.
 * ${APP_NAME}
 */
class GoalListAdapter(private val goalItemList: ArrayList<GoalItemDto>, private val goalSelectedListener: GoalSelectedListener):
        RecyclerView.Adapter<GoalListAdapter.GoalViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder(parent.inflate(R.layout.goal_list_adapter_row))
    }

    override fun getItemCount(): Int {
        return goalItemList.size
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(goalItemList[position], goalSelectedListener)
    }


    class GoalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(goalItemDto: GoalItemDto, goalSelectedListener: GoalSelectedListener) = with(itemView) {
            val rewardContent = String.format(itemView.context.getString(R.string.reward_text),
                    goalItemDto.reward.trophy,
                    goalItemDto.reward.points.toString())

            title.text = goalItemDto.title
            description.text = goalItemDto.description
            reward.text = rewardContent


            this.setOnClickListener {
                goalSelectedListener.onGoalSelected(goalItemDto)
            }
        }
    }

    interface GoalSelectedListener {
        fun onGoalSelected(goalItemDto: GoalItemDto)
    }
}
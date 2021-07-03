package com.trisys.rn.baseapp.adapter.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.onBoarding.AttemptedTest
import kotlinx.android.synthetic.main.all_results_item.view.*

class AllResultsAdapter(
    val context: Context,
    private val scheduledTestItems: List<AttemptedTest>,
) : RecyclerView.Adapter<AllResultsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.all_results_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduledTest = scheduledTestItems[position]
        holder.itemView.testNameTxt.text = scheduledTest.name
        holder.itemView.scoreAnstxt.text = scheduledTest.name
        holder.itemView.highscoreAnstxt.text = scheduledTest.name
        holder.itemView.rankAnstxt.text = scheduledTest.name
        holder.itemView.attemptTxtAns.text = scheduledTest.name
    }

    override fun getItemCount(): Int {
        return scheduledTestItems.size
    }
}
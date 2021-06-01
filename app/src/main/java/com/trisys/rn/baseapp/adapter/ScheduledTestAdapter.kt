package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.ScheduledTestItem
import kotlinx.android.synthetic.main.row_completed_live.view.backgroundColor
import kotlinx.android.synthetic.main.row_scheduled_test.view.*


class ScheduledTestAdapter(
    val context: Context,
    private val scheduledTestItems: ArrayList<ScheduledTestItem>,
    private var testClickListener: TestClickListener
) : RecyclerView.Adapter<ScheduledTestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_scheduled_test, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduledTest = scheduledTestItems[position]
        holder.itemView.testName.text = scheduledTest.testName
        holder.itemView.backgroundColor.setBackgroundColor(context.getColor(scheduledTest.color))
        holder.itemView.marks.text = scheduledTest.mark
        holder.itemView.date.text = scheduledTest.date
        holder.itemView.duration.text = scheduledTest.duration

        holder.itemView.takeTest.setOnClickListener {
            testClickListener.onTestClicked(true)
        }
    }

    override fun getItemCount(): Int {
        return scheduledTestItems.size
    }
}
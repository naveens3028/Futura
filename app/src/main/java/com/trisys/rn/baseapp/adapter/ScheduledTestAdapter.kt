package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.MergedTest
import com.trisys.rn.baseapp.utils.Utils
import com.trisys.rn.baseapp.utils.Utils.getDateValue
import kotlinx.android.synthetic.main.row_scheduled_test.view.*


class ScheduledTestAdapter(
    val context: Context,
    private val scheduledItems: List<MergedTest>,
    private var testClickListener: TestClickListener
) : RecyclerView.Adapter<ScheduledTestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_scheduled_test, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduledTest = scheduledItems[position]
        holder.itemView.testName.text = scheduledTest.name
        holder.itemView.marks.text = scheduledTest.questionCount.toString()
        Utils.testLog(scheduledTest.publishDate.toString())
        holder.itemView.date.text = getDateValue(scheduledTest.publishDate)
        holder.itemView.duration.text = scheduledTest.duration.toString()
        holder.itemView.takeTest.setOnClickListener {
            testClickListener.onTestClicked(true, scheduledTest)
        }
    }

    override fun getItemCount(): Int {
        return scheduledItems.size
    }

}
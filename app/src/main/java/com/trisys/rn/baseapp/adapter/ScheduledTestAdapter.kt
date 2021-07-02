package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.utils.Utils.getDateValue
import kotlinx.android.synthetic.main.row_scheduled_test.view.*
import java.text.SimpleDateFormat
import java.util.*


class ScheduledTestAdapter(
    val context: Context,
    private val scheduledItems: List<MOCKTEST>,
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
        holder.itemView.testName.text = scheduledTest.testPaperVo.name
        holder.itemView.marks.text =
            (scheduledTest.testPaperVo.questionCount * scheduledTest.testPaperVo.correctMark).toString()
        holder.itemView.date.text = getDateValue(scheduledTest.publishDateTime)
        holder.itemView.duration.text = scheduledTest.testPaperVo.duration.toString()
        holder.itemView.takeTest.setOnClickListener {
            testClickListener.onTestClicked(true,scheduledTest)
        }
    }

    override fun getItemCount(): Int {
        return scheduledItems.size
    }

}
package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.model.ScheduledTestItem
import kotlinx.android.synthetic.main.row_completed_live.view.backgroundColor
import kotlinx.android.synthetic.main.row_scheduled_test.view.*
import java.text.SimpleDateFormat
import java.util.*


class ScheduledTestAdapter(
    val context: Context,
    private val scheduledTestItems: ArrayList<ScheduledTestItem>,
    private val scheduledItems: List<MOCKTEST>? = null,
    private var testClickListener: TestClickListener
) : RecyclerView.Adapter<ScheduledTestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_scheduled_test, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (scheduledItems != null && scheduledItems.isNotEmpty()) {
            val scheduledTest = scheduledItems[position]
            holder.itemView.testName.text = scheduledTest.testPaperVo.name
            holder.itemView.marks.text =
                (scheduledTest.testPaperVo.questionCount * scheduledTest.testPaperVo.correctMark).toString()
            holder.itemView.date.text =
                getDate(scheduledTest.testPaperVo.updatedAt, "dd/MM/yyyy hh:mm:ss.SSS").toString()
            holder.itemView.duration.text = scheduledTest.testPaperVo.duration.toString()
            holder.itemView.takeTest.setOnClickListener {
                testClickListener.onTestClicked(true)
            }
        } else {
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
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        val formatter = SimpleDateFormat(dateFormat)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    override fun getItemCount(): Int {
        if (scheduledItems != null && scheduledItems.isNotEmpty()) {
            return scheduledItems.size
        }
        return scheduledTestItems.size
    }
}
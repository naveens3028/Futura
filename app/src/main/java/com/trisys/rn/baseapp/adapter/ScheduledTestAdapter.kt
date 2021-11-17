package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.utils.Utils.getDateTime
import com.trisys.rn.baseapp.utils.Utils.getDateValue
import com.trisys.rn.baseapp.utils.Utils.getLocaleTime
import kotlinx.android.synthetic.main.row_practice_test.view.*
import kotlinx.android.synthetic.main.row_scheduled_test.view.*


class ScheduledTestAdapter(
    val context: Context,
    private val scheduledItems: List<MOCKTEST>,
    private var testClickListener: TestClickListener
) : RecyclerView.Adapter<ScheduledTestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_practice_test, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduledTest = scheduledItems[position]
        holder.itemView.topicPractice.text = scheduledTest.testPaperVo?.name
        holder.itemView.noOfQuesTxt.text = scheduledTest.testPaperVo?.questionCount.toString() + " Question"
        holder.itemView.durationTxtPrac.text = getDateTime(scheduledTest.publishDate!!)
        holder.itemView.takeTestPrac.setOnClickListener {
            testClickListener.onTestClicked(true, scheduledTest)
        }
    }

    override fun getItemCount(): Int {
        return scheduledItems.size
    }

}
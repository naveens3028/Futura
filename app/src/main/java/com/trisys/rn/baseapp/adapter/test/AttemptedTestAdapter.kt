package com.trisys.rn.baseapp.adapter.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.TestClickListener
import com.trisys.rn.baseapp.model.onBoarding.AttemptedTest
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.row_scheduled_test.view.*

class AttemptedTestAdapter(
    val context: Context,
    private val scheduledTestItems: List<AttemptedTest>,
    private var testClickListener: TestClickListener
) : RecyclerView.Adapter<AttemptedTestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_scheduled_test, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduledTest = scheduledTestItems[position]
        holder.itemView.testName.text = scheduledTest.name
        holder.itemView.backgroundColor.setBackgroundColor(context.getColor(R.color.carolina_blue))
        holder.itemView.marks.text =
            (scheduledTest.questionCount * scheduledTest.correctMarks).toString()
        holder.itemView.date.text = Utils.getDateValue(scheduledTest.publishDateTime)
        holder.itemView.duration.text = scheduledTest.duration.toString()
        holder.itemView.takeTest.text = "Result"
        holder.itemView.takeTest.setOnClickListener {
            testClickListener.onResultClicked(true)
        }
    }

    override fun getItemCount(): Int {
        return scheduledTestItems.size
    }
}
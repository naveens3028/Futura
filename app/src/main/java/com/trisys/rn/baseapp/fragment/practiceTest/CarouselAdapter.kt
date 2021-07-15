package com.trisys.rn.baseapp.fragment.practiceTest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.onBoarding.AverageBatchTests
import com.trisys.rn.baseapp.utils.Utils.getMarkPercentage
import kotlinx.android.synthetic.main.row_average_student.view.*
import kotlinx.android.synthetic.main.row_chart.view.*

class CarouselAdapter(
    val context: Context,
    private val testResult: MutableList<AverageBatchTests>
) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        const val VIEW_TYPE_THREE = 3
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = when (viewType) {
            VIEW_TYPE_ONE -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_average_student, parent, false)
            }
            VIEW_TYPE_TWO -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.activity_subjects, parent, false)
            }
            else -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_overall_result, parent, false)
            }
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.itemView.stud_rank.text = testResult[0].rank.toString()
                holder.itemView.yourAvgScoretxt.text =
                    getMarkPercentage(testResult[0].studentAverage)
                holder.itemView.classAvgtxt.text = getMarkPercentage(testResult[0].classAverage)
                holder.itemView.topperAvgTxt.text = getMarkPercentage(testResult[0].topperAverage)
                holder.itemView.outOfStud.text = "Out of ${testResult[0].rank} Students "
            }
            1 -> {
                initChart(holder.itemView)
            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_ONE
            1 -> VIEW_TYPE_TWO
            else -> VIEW_TYPE_THREE
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    private fun initChart(view: View) {

        //values for data input Dataset1 at your axis one positions
        val dataset1 = ArrayList<Entry>()
        dataset1.add(Entry(0f, 420f))
        dataset1.add(Entry(1f, 360f))
        dataset1.add(Entry(2f, 570f))
        dataset1.add(Entry(3f, 355f))
        dataset1.add(Entry(4f, 480f))

        val vl = LineDataSet(dataset1, "Your Score")
        vl.setDrawValues(false)
        vl.setDrawFilled(false)
        vl.color = ContextCompat.getColor(context, R.color.caribbean_green)
        vl.lineWidth = 2f
        vl.setDrawCircles(false)
        vl.setDrawHighlightIndicators(false)

        //values for data input Dataset1 at your axis one positions
        val dataset2 = ArrayList<Entry>()
        dataset2.add(Entry(0f, 330f))
        dataset2.add(Entry(1f, 680f))
        dataset2.add(Entry(2f, 470f))
        dataset2.add(Entry(3f, 255f))
        dataset2.add(Entry(4f, 580f))

        val vl2 = LineDataSet(dataset2, "Topper's Score")
        vl2.setDrawValues(false)
        vl2.setDrawFilled(false)
        vl2.setDrawCircles(false)
        vl2.lineWidth = 2f
        vl2.color = ContextCompat.getColor(context, R.color.bittersweet)
        vl2.setDrawHighlightIndicators(false)

        val xLabel: ArrayList<String> = ArrayList()
        xLabel.add("20/07")
        xLabel.add("22/07")
        xLabel.add("28/07")
        xLabel.add("30/07")
        xLabel.add("1/8")

        val chartData = LineData()
        chartData.addDataSet(vl)
        chartData.addDataSet(vl2)

        val tf = ResourcesCompat.getFont(context, R.font.roboto_regular)

        view.lineCharttest.setTouchEnabled(true)
        view.lineCharttest.setPinchZoom(true)
        view.lineCharttest.xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
        view.lineCharttest.xAxis.position = XAxis.XAxisPosition.BOTTOM
        view.lineCharttest.xAxis.granularity = 1f
        view.lineCharttest.axisLeft.axisMinimum = 0f
        view.lineCharttest.axisLeft.axisMaximum = 720f
        view.lineCharttest.axisLeft.setLabelCount(13, true)
        view.lineCharttest.data = chartData
        view.lineCharttest.axisRight.isEnabled = false
        view.lineCharttest.invalidate()
        view.lineCharttest.xAxis.yOffset = 10f
        view.lineCharttest.axisLeft.xOffset = 15f
        view.lineCharttest.legend.textSize = 10f
        view.lineCharttest.axisLeft.textSize = 10f
        view.lineCharttest.xAxis.textSize = 10f
        view.lineCharttest.legend.typeface = tf
        view.lineCharttest.xAxis.typeface = tf
        view.lineCharttest.axisLeft.typeface = tf
        view.lineCharttest.legend.textColor =
            ContextCompat.getColor(context, R.color.text_color)
        view.lineCharttest.axisLeft.textColor =
            ContextCompat.getColor(context, R.color.text_color)
        view.lineCharttest.xAxis.textColor = ContextCompat.getColor(context, R.color.text_color)
        view.lineCharttest.legend.textColor =
            ContextCompat.getColor(context, R.color.text_color)
        view.lineCharttest.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        view.lineCharttest.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        view.lineCharttest.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        view.lineCharttest.setExtraOffsets(0f, 0f, 0f, 10f)
        view.lineCharttest.legend.form = Legend.LegendForm.LINE
        view.lineCharttest.description.isEnabled = false
        view.lineCharttest.setNoDataText("No Test yet!")
        view.lineCharttest.animateX(1800, Easing.EaseInExpo)

    }
}
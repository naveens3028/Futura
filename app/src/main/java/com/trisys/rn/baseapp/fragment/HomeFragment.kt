package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.trisys.rn.baseapp.Model.CompletedLiveItem
import com.trisys.rn.baseapp.Model.StudyItem
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.CompletedLiveAdapter
import com.trisys.rn.baseapp.adapter.StudyAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private var studyList = ArrayList<StudyItem>()
    private var completedLiveList = ArrayList<CompletedLiveItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChart()

        //Sample Data
        studyList.add(
            StudyItem(
                "Mathematics",
                "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.blue_munsell
            )
        )
        studyList.add(
            StudyItem(
                "Physics", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.plump_purple
            )
        )
        studyList.add(
            StudyItem(
                "Chemistry", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.satin_sheen_gold
            )
        )
        studyList.add(
            StudyItem(
                "Biology", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.super_pink
            )
        )

        completedLiveList.add(
            CompletedLiveItem(
                "Chemistry",
                R.drawable.chemistry,
                R.color.satin_sheen_gold
            )
        )
        completedLiveList.add(
            CompletedLiveItem(
                "Physics",
                R.drawable.physics,
                R.color.plump_purple
            )
        )
        completedLiveList.add(CompletedLiveItem("Biology", R.drawable.biology, R.color.super_pink))
        completedLiveList.add(
            CompletedLiveItem(
                "Mathematics",
                R.drawable.maths,
                R.color.blue_munsell
            )
        )

        val studyRecyclerView = view.findViewById(R.id.studyRecycler) as RecyclerView
        val studyAdapter = StudyAdapter(context!!, studyList)
        studyRecyclerView.adapter = studyAdapter

        val completedLiveAdapter = CompletedLiveAdapter(context!!, completedLiveList)
        upcomingLiveLiveRecycler.adapter = completedLiveAdapter
    }

    private fun initChart() {

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
        vl.color = ContextCompat.getColor(context!!, R.color.medium_sea_green)
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
        vl2.color = ContextCompat.getColor(context!!, R.color.bittersweet)
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

        val tf = ResourcesCompat.getFont(context!!, R.font.roboto_regular)

        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.granularity = 1f
        lineChart.axisLeft.axisMinimum = 0f
        lineChart.axisLeft.axisMaximum = 720f
        lineChart.axisLeft.setLabelCount(13, true)
        lineChart.data = chartData
        lineChart.axisRight.isEnabled = false
        lineChart.invalidate()
        lineChart.xAxis.yOffset = 10f
        lineChart.axisLeft.xOffset = 15f
        lineChart.legend.textSize = 10f
        lineChart.axisLeft.textSize = 10f
        lineChart.xAxis.textSize = 10f
        lineChart.legend.typeface = tf
        lineChart.xAxis.typeface = tf
        lineChart.axisLeft.typeface = tf
        lineChart.legend.textColor = ContextCompat.getColor(context!!, R.color.davys_grey)
        lineChart.axisLeft.textColor = ContextCompat.getColor(context!!, R.color.davys_grey)
        lineChart.xAxis.textColor = ContextCompat.getColor(context!!, R.color.davys_grey)
        lineChart.legend.textColor = ContextCompat.getColor(context!!, R.color.davys_grey)
        lineChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        lineChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        lineChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        lineChart.setExtraOffsets(0f, 0f, 0f, 10f)
        lineChart.legend.form = Legend.LegendForm.LINE
        lineChart.description.isEnabled = false
        lineChart.setNoDataText("No Test yet!")

        lineChart.animateX(1800, Easing.EaseInExpo)

    }

}
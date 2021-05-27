package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.trisys.rn.baseapp.model.StudyItem
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.StudyAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private var studyList = ArrayList<StudyItem>()
    private lateinit var fragment: Fragment

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

        fragment = UpcomingLiveFragment()
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameLayout, fragment)
        fragmentTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction?.commit()


        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> fragment = UpcomingLiveFragment()
                    1 -> fragment = ScheduledTestFragment()
                }
                val fm: FragmentManager? = activity?.supportFragmentManager
                val ft = fm?.beginTransaction()
                ft?.replace(R.id.frameLayout, fragment)
                ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft?.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        //Sample Data
        studyList.add(
            StudyItem(
                "Mathematics",
                "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.caribbean_green
            )
        )
        studyList.add(
            StudyItem(
                "Physics", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.blue_violet_crayola
            )
        )
        studyList.add(
            StudyItem(
                "Chemistry", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.safety_yellow
            )
        )
        studyList.add(
            StudyItem(
                "Biology", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.light_coral
            )
        )

        val studyRecyclerView = view.findViewById(R.id.studyRecycler) as RecyclerView
        val studyAdapter = StudyAdapter(requireContext(), studyList)
        studyRecyclerView.adapter = studyAdapter

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
        vl.color = ContextCompat.getColor(requireContext(), R.color.emerald)
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
        vl2.color = ContextCompat.getColor(requireContext(), R.color.bittersweet)
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

        val tf = ResourcesCompat.getFont(requireContext(), R.font.roboto_regular)

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
        lineChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.text_color)
        lineChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.text_color)
        lineChart.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text_color)
        lineChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.text_color)
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
package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.util.Log
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.HomeStudyAdapter
import com.trisys.rn.baseapp.database.AppDatabase
import com.trisys.rn.baseapp.fragment.practiceTest.ScheduledTestFragment
import com.trisys.rn.baseapp.model.StudyItem
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    lateinit var db: AppDatabase
    private var studyList = ArrayList<StudyItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getInstance(requireContext())!!
        initChart()

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
        val studyAdapter = HomeStudyAdapter(requireContext(), studyList)
        studyRecyclerView.adapter = studyAdapter

    }

    override fun onStart() {
        super.onStart()

        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, UpcomingLiveFragment.newInstance("", "")).commit()

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 ->
                        childFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, UpcomingLiveFragment.newInstance("", ""))
                            .commit()
                    1 ->
                        childFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, ScheduledTestFragment())
                            .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        Log.e("popTable", db.videoDao.getAll().toString())

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LearnFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
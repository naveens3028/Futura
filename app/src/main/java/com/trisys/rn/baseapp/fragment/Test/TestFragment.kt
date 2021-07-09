package com.trisys.rn.baseapp.fragment.Test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.androidnetworking.common.Priority
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.AttemptedResultsActivity
import com.trisys.rn.baseapp.database.AppDatabase
import com.trisys.rn.baseapp.database.DatabaseHelper
import com.trisys.rn.baseapp.model.StudyItem
import com.trisys.rn.baseapp.model.onBoarding.AverageBatchTests
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_test.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestFragment : Fragment(), OnNetworkResponse {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    private var averBatchTest = mutableListOf<AverageBatchTests>()
    lateinit var myPreferences: MyPreferences
    lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        db = DatabaseHelper(requireContext())
        initChart()
        averBatchTest = db.getAllAverageBatchTest()

        Log.e("avgBatch", averBatchTest.toString())
        if (averBatchTest.isNullOrEmpty()) {
            requestSessions()
        }else{
            assessmentSetup(averBatchTest)
        }

        allResults.setOnClickListener {
            val intent = Intent(requireContext(), AttemptedResultsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayout1, TestTabFragment.newInstance("", "")).commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> childFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout1, TestTabFragment.newInstance("", "")).commit()

                    1 -> childFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout1, PracticeTabFragment.newInstance("", ""))
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    private fun requestSessions() {

        val params = HashMap<String, String>()
        params["batchId"] = loginData.userDetail?.batchIds?.get(0).toString()
        params["studentId"] = loginData.userDetail?.usersId.toString()

        networkHelper.call(
            networkHelper.GET,
            networkHelper.RESTYPE_OBJECT,
            URLHelper.averageBatchTests,
            params,
            Priority.HIGH,
            "getAssessments",
            this
        )

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
        vl.color = ContextCompat.getColor(requireContext(), R.color.caribbean_green)
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

        lineCharttest.setTouchEnabled(true)
        lineCharttest.setPinchZoom(true)
        lineCharttest.xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
        lineCharttest.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineCharttest.xAxis.granularity = 1f
        lineCharttest.axisLeft.axisMinimum = 0f
        lineCharttest.axisLeft.axisMaximum = 720f
        lineCharttest.axisLeft.setLabelCount(13, true)
        lineCharttest.data = chartData
        lineCharttest.axisRight.isEnabled = false
        lineCharttest.invalidate()
        lineCharttest.xAxis.yOffset = 10f
        lineCharttest.axisLeft.xOffset = 15f
        lineCharttest.legend.textSize = 10f
        lineCharttest.axisLeft.textSize = 10f
        lineCharttest.xAxis.textSize = 10f
        lineCharttest.legend.typeface = tf
        lineCharttest.xAxis.typeface = tf
        lineCharttest.axisLeft.typeface = tf
        lineCharttest.legend.textColor =
            ContextCompat.getColor(requireContext(), R.color.text_color)
        lineCharttest.axisLeft.textColor =
            ContextCompat.getColor(requireContext(), R.color.text_color)
        lineCharttest.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text_color)
        lineCharttest.legend.textColor =
            ContextCompat.getColor(requireContext(), R.color.text_color)
        lineCharttest.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        lineCharttest.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        lineCharttest.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        lineCharttest.setExtraOffsets(0f, 0f, 0f, 10f)
        lineCharttest.legend.form = Legend.LegendForm.LINE
        lineCharttest.description.isEnabled = false
        lineCharttest.setNoDataText("No Test yet!")
        lineCharttest.animateX(1800, Easing.EaseInExpo)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        Log.e("poppers", "response: $response  tags: $tag  responseCode: $responseCode.toString()")
        if (tag.equals("getAssessments")) {
            val testResponse = Gson().fromJson(response, AverageBatchTests::class.java)
            db.saveAvg(testResponse)
            assessmentSetup(db.getAllAverageBatchTest())
        }
    }

    private fun assessmentSetup(testResult: MutableList<AverageBatchTests>){
        stud_rank.text = testResult[0].rank.toString()
        yourAvgScoretxt.text = String.format("%.2f", testResult[0].studentAverage)+"%"
        classAvgtxt.text = String.format("%.2f", testResult[0].classAverage)+"%"
        topperAvgTxt.text = String.format("%.2f", testResult[0].topperAverage)+"%"
        outOfStud.text = "Out of ${testResult[0].rank} Students "
    }
}
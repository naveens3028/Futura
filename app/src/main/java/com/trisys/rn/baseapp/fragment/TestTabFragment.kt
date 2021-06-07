package com.trisys.rn.baseapp.fragment

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.TakeTestActivity
import com.trisys.rn.baseapp.adapter.ScheduledTestAdapter
import com.trisys.rn.baseapp.adapter.TestClickListener
import com.trisys.rn.baseapp.model.ScheduledTestItem
import com.trisys.rn.baseapp.model.SubTopicItem
import kotlinx.android.synthetic.main.fragment_test_tab.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class TestTabFragment : Fragment() , TestClickListener{

    private var studyList = ArrayList<SubTopicItem>()
    private var scheduledTestList = ArrayList<ScheduledTestItem>()
    private var checkVisible: Boolean? = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        scheduledTestList.add(
            ScheduledTestItem(
                "JEE Mains Test2",
                "17th Mar, 9:30AM",
                "120",
                "1h 25m",
                R.color.caribbean_green
            )
        )
        scheduledTestList.add(
            ScheduledTestItem(
                "NEET Mains Test2",
                "17th Mar, 9:30AM",
                "120", "1h 25m",
                R.color.light_coral
            )
        )
        scheduledTestList.add(
            ScheduledTestItem(
                "NCERT Mains Test2",
                "19th Mar, 9:30AM",
                "120", "1h 25m",
                R.color.blue_violet_crayola
            )
        )
        scheduledTestList.add(
            ScheduledTestItem(
                "NEET Mains Test3",
                "19th Mar, 9:30AM",
                "120", "1h 25m",
                R.color.caribbean_green
            )
        )


        val studyAdapter = ScheduledTestAdapter(requireContext(), scheduledTestList,this)
        scheduleTestRecyclerView.adapter = studyAdapter

        arrowscheduled.setOnClickListener {
            if (checkVisible == false) {
                scheduleTestRecyclerView.visibility = View.GONE
                checkVisible = true
            }else{
                scheduleTestRecyclerView.visibility = View.VISIBLE
                checkVisible = false
            }
        }

    }

    override fun onTestClicked(isClicked: Boolean) {
        val intent = Intent(requireContext(), TakeTestActivity::class.java)
        startActivity(intent)
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
            TestTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.ScheduledTestAdapter
import com.trisys.rn.baseapp.model.ScheduledTestItem
import kotlinx.android.synthetic.main.fragment_upcoming_live.*

class ScheduledTestFragment : Fragment() {

    private var scheduledTestList = ArrayList<ScheduledTestItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scheduled_test, container, false)
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

        val completedLiveAdapter = ScheduledTestAdapter(requireContext(), scheduledTestList)
        recycler.adapter = completedLiveAdapter
    }

}
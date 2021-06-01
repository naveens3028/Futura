package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trisys.rn.baseapp.model.CompletedLiveItem
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.CompletedLiveAdapter
import kotlinx.android.synthetic.main.fragment_upcoming_live.*

class UpcomingLiveFragment : Fragment() {

    private var completedLiveList = ArrayList<CompletedLiveItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        completedLiveList.add(
            CompletedLiveItem(
                "Chemistry",
                "",
                "L2 -Chemical Reaction and Periodic Table",
                R.color.safety_yellow
            )
        )
        completedLiveList.add(
            CompletedLiveItem(
                "Physics",
                "",
                "L3 - Universe, Galaxy and Solar System",
                R.color.blue_violet_crayola
            )
        )
        completedLiveList.add(
            CompletedLiveItem(
                "Biology",
                "",
                "L4 - Digestive System and Human Brain",
                R.color.light_coral
            )
        )
        completedLiveList.add(CompletedLiveItem("Mathematics", "", "L5 - Trigonometry and Functions", R.color.caribbean_green))

        val completedLiveAdapter = CompletedLiveAdapter(requireContext(), completedLiveList)
        recycler.adapter = completedLiveAdapter
    }
}
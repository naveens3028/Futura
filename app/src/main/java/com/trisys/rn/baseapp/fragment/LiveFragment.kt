package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.model.CompletedLiveItem
import com.trisys.rn.baseapp.model.UpcomingLiveItem
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.CompletedLiveAdapter
import com.trisys.rn.baseapp.adapter.UpcomingLiveAdapter

class LiveFragment : Fragment() {

    private var upcomingLiveList = ArrayList<UpcomingLiveItem>()
    private var completedLiveList = ArrayList<CompletedLiveItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingLiveList.add(UpcomingLiveItem("Mathematics", R.drawable.mathematics))
        upcomingLiveList.add(UpcomingLiveItem("Physics", R.drawable.mathematics))
        upcomingLiveList.add(UpcomingLiveItem("Chemistry", R.drawable.mathematics))
        upcomingLiveList.add(UpcomingLiveItem("Biology", R.drawable.mathematics))


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
        completedLiveList.add(
            CompletedLiveItem(
                "Mathematics",
                "", "L5 - Trigonometry and Functions",
                R.color.caribbean_green
            )
        )

        val upcomingLiveRecyclerView = view.findViewById(R.id.upcomingLiveRecycler) as RecyclerView
        upcomingLiveRecyclerView.smoothScrollToPosition(0)
        val upcomingLiveAdapter = UpcomingLiveAdapter(requireContext(), upcomingLiveList)
        upcomingLiveRecyclerView.adapter = upcomingLiveAdapter

        val completedLiveRecyclerView =
            view.findViewById(R.id.completedLiveRecycler) as RecyclerView
        val completedLiveAdapter = CompletedLiveAdapter(requireContext(), completedLiveList)
        completedLiveRecyclerView.adapter = completedLiveAdapter
    }
}
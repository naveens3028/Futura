package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.Model.CompletedLiveItem
import com.trisys.rn.baseapp.Model.UpcomingLiveItem
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

        val upcomingLiveRecyclerView = view.findViewById(R.id.upcomingLiveRecycler) as RecyclerView
        upcomingLiveRecyclerView.smoothScrollToPosition(0)
        val upcomingLiveAdapter = UpcomingLiveAdapter(context!!, upcomingLiveList)
        upcomingLiveRecyclerView.adapter = upcomingLiveAdapter

        val completedLiveRecyclerView =
            view.findViewById(R.id.completedLiveRecycler) as RecyclerView
        val completedLiveAdapter = CompletedLiveAdapter(context!!, completedLiveList)
        completedLiveRecyclerView.adapter = completedLiveAdapter
    }
}
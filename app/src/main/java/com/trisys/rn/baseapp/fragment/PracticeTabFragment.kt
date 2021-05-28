package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.model.CompletedLiveItem
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.CompletedLiveAdapter
import com.trisys.rn.baseapp.adapter.StudyAdapter
import com.trisys.rn.baseapp.model.StudyItem
import com.trisys.rn.baseapp.model.SubTopicItem
import kotlinx.android.synthetic.main.fragment_upcoming_live.*

class PracticeTabFragment : Fragment() {

    private var studyList = ArrayList<SubTopicItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_practice_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Sample Data
        studyList.add(
            SubTopicItem(
                "JEE",
            )
        )
        studyList.add(
            SubTopicItem(
                "NEET",
            )
        )
        studyList.add(
            SubTopicItem(
                "NEET 2",
            )
        )

//        val studyRecyclerView = view.findViewById(R.id.studyRecycler) as RecyclerView
//        val studyAdapter = StudyAdapter(requireContext(), studyList)
//        studyRecyclerView.adapter = studyAdapter
//
//        val completedLiveAdapter = CompletedLiveAdapter(requireContext(), completedLiveList)
//        recycler.adapter = completedLiveAdapter
    }
}
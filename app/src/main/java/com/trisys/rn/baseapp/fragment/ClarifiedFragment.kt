package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.ClarifiedDoubtAdapter
import com.trisys.rn.baseapp.model.ClarifiedDoubtItem
import kotlinx.android.synthetic.main.fragment_clarified.*

class ClarifiedFragment : Fragment() {

    private var studyList = ArrayList<ClarifiedDoubtItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clarified, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studyList.add(
            ClarifiedDoubtItem(
                "Mathematics",
                "Basic of Trigonometry",
                "19th Mar,\n9:30AM", R.color.caribbean_green
            )
        )
        studyList.add(
            ClarifiedDoubtItem(
                "Biology",
                "Digestive System",
                "18th Mar,\n9:30AM", R.color.light_coral
            )
        )
        studyList.add(
            ClarifiedDoubtItem(
                "Physics",
                "Solar System",
                "17th Mar,\n9:30AM", R.color.blue_violet_crayola
            )
        )
        studyList.add(
            ClarifiedDoubtItem(
                "Chemistry",
                "Periodic Table",
                "16th Mar,\n9:30AM", R.color.safety_yellow
            )
        )

        val studyAdapter = ClarifiedDoubtAdapter(requireContext(), studyList)
        clarifiedRecycler.adapter = studyAdapter
    }
}
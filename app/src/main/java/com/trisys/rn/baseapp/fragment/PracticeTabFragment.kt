package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.PracticeSubjectAdapter
import com.trisys.rn.baseapp.model.PracticeSubjects
import kotlinx.android.synthetic.main.fragment_practice_tab.*

class PracticeTabFragment : Fragment() {

    private var subjectList = ArrayList<PracticeSubjects>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_practice_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subjectList.add(PracticeSubjects("Physics", "10.11%"))
        subjectList.add(PracticeSubjects("Chemistry", "10.11%"))
        subjectList.add(PracticeSubjects("Biology", "10.11%"))

        val studyAdapter = PracticeSubjectAdapter(requireContext(), subjectList)
        practiceRecyclerView.adapter = studyAdapter
    }
}
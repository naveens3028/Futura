package com.upmyranksapp.fragment.practiceTest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.upmyranksapp.R
import com.upmyranksapp.adapter.PracticeSubjectAdapter
import com.upmyranksapp.model.PracticeSubjects
import com.upmyranksapp.practiceTest.TestTopicActivity
import kotlinx.android.synthetic.main.fragment_practice_tab.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
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

        crd_createprac.setOnClickListener {
            val intent = Intent(requireContext(), TestTopicActivity::class.java)
            requireContext().startActivity(intent)
        }

        subjectList.add(PracticeSubjects("Physics", "10.11%"))
        subjectList.add(PracticeSubjects("Chemistry", "10.11%"))
        subjectList.add(PracticeSubjects("Biology", "10.11%"))

        val studyAdapter = PracticeSubjectAdapter(requireContext(), subjectList)
        practiceRecyclerView.adapter = studyAdapter
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
            PracticeTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
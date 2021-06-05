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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
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
            UpcomingLiveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
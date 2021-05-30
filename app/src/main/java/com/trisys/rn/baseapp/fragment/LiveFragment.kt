package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.StudyAdapter
import com.trisys.rn.baseapp.model.StudyItem
import com.trisys.rn.baseapp.model.UpcomingLiveItem
import kotlinx.android.synthetic.main.fragment_live.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LiveFragment : Fragment() {

    private var upcomingLiveList = ArrayList<UpcomingLiveItem>()
    private var studyList = ArrayList<StudyItem>()
    private lateinit var fragment: Fragment

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

        fragment = UpcomingLiveFragment()
        Log.d("s2s","saravana 3")
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.liveFrameLayout, fragment)
        fragmentTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction?.commit()
        Log.d("s2s","saravana 4")


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        fragment = UpcomingLiveFragment()
                        Log.d("s2s","saravana 1")
                    }
                    1 -> {
                        fragment = UpcomingLiveFragment()
                        Log.d("s2s","saravana 2")
                    }
                }
                val fm: FragmentManager? = activity?.supportFragmentManager
                val ft = fm?.beginTransaction()
                ft?.replace(R.id.frameLayout, fragment)
                ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft?.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //Sample Data
        studyList.add(
            StudyItem(
                "Mathematics",
                "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.caribbean_green
            )
        )
        studyList.add(
            StudyItem(
                "Physics", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.blue_violet_crayola
            )
        )
        studyList.add(
            StudyItem(
                "Chemistry", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.safety_yellow
            )
        )
        studyList.add(
            StudyItem(
                "Biology", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.light_coral
            )
        )

        val studyRecyclerView = view.findViewById(R.id.studyRecycler) as RecyclerView
        val studyAdapter = StudyAdapter(requireContext(), studyList)
        studyRecyclerView.adapter = studyAdapter

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
            LiveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
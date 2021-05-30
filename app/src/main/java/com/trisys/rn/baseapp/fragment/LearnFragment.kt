package com.trisys.rn.baseapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.model.Subjects
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.CourseAdapter
import com.trisys.rn.baseapp.adapter.SubjectClickListener
import com.trisys.rn.baseapp.adapter.SubjectListAdapter
import com.trisys.rn.baseapp.adapter.SubjectsAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LearnFragment : Fragment(), SubjectClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var pageNo: Int? = null
    private lateinit var subjectRecycler: RecyclerView
    private lateinit var courseRecycler: RecyclerView
    private lateinit var subjectRecyclerList: RecyclerView
    private var subjectList = ArrayList<Subjects>()
    private var courseList = ArrayList<String>()
    private var chapterList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learn, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageNo = 1
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                //pageNo = 1
                courseRecycler.visibility = View.VISIBLE
                subjectRecycler.visibility = View.VISIBLE
                subjectRecyclerList.visibility = View.GONE
            }
        })
        subjectRecycler = view.findViewById(R.id.recyclerview) as RecyclerView
        courseRecycler = view.findViewById(R.id.recyclerviewcourse) as RecyclerView
        subjectRecyclerList = view.findViewById(R.id.recyclerviewsubjectslist) as RecyclerView

        subjectList.add(Subjects("Physics", R.drawable.physics))
        subjectList.add(Subjects("Chemistry", R.drawable.chemistry))
        subjectList.add(Subjects("Biology", R.drawable.biology))
        subjectList.add(Subjects("Mathematics", R.drawable.maths))

        courseList.add("NCERT")
        courseList.add("NEET")
        courseList.add("JEE MAINS")

        chapterList.apply {
            this.add("Physics World")
            this.add("Law of Motions")
            this.add("Conservation of Energy")
            this.add("Heat and Temperature")
            this.add("Wave Energy")
            this.add("Kinematics")
            this.add("Dynamics: Forces and Motion")
            this.add("Impulse and Momentum")
            this.add("Astronomy")
            this.add("Electricity and Electrical Energy")
            this.add("Nature and Behavior of Light")
        }

        subjectCall()
        courseCall()
        subjectListCall()
    }

    private fun subjectCall() {
        //adding a layoutmanager
        subjectRecycler.layoutManager = GridLayoutManager(context, 2)
        val adapter = SubjectsAdapter(requireContext(), subjectList, this)

        //now adding the adapter to recyclerview
        subjectRecycler.adapter = adapter
    }

    @SuppressLint("WrongConstant")
    private fun courseCall() {
        courseRecycler.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)

        courseRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.HORIZONTAL
            )
        )
        val adapter = CourseAdapter(requireContext(), courseList)

        //now adding the adapter to recyclerview
        courseRecycler.adapter = adapter
    }

    @SuppressLint("WrongConstant")
    private fun subjectListCall() {
        //adding a layoutmanager
        subjectRecyclerList.layoutManager =
            LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        val adapter = SubjectListAdapter(requireContext(), chapterList)

        //now adding the adapter to recyclerview
        subjectRecyclerList.adapter = adapter
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
            LearnFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSubjectClicked(isClicked: Boolean) {
        pageNo = 2
        courseRecycler.visibility = View.GONE
        subjectRecycler.visibility = View.GONE
        subjectRecyclerList.visibility = View.VISIBLE
    }

}
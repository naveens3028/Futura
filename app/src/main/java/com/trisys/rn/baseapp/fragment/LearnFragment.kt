package com.trisys.rn.baseapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.Model.Subjects
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.CourseAdapter
import com.trisys.rn.baseapp.adapter.SubjectsAdapter
import kotlinx.android.synthetic.main.fragment_learn.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LearnFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var subjectRecycler: RecyclerView
    private lateinit var courseRecycler: RecyclerView
    private var subjectList = ArrayList<Subjects>()
    private var courseList = ArrayList<String>()

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

        subjectList.add(Subjects("Physics", R.drawable.physics))
        subjectList.add(Subjects("Chemistry", R.drawable.chemistry))
        subjectList.add(Subjects("Biology", R.drawable.biology))
        subjectList.add(Subjects("Mathematics", R.drawable.maths))

        courseList.add("NCERT")
        courseList.add("NEET")
        courseList.add("JEE MAINS")

        subjectRecycler = view.findViewById(R.id.recyclerview) as RecyclerView
        courseRecycler = view.findViewById(R.id.recyclerviewcourse) as RecyclerView

        subjectCall()
        courseCall()

    }

    private fun subjectCall(){
        //adding a layoutmanager
        subjectRecycler.layoutManager = GridLayoutManager(context, 2)
        val adapter = SubjectsAdapter(context!!,subjectList)

        //now adding the adapter to recyclerview
        subjectRecycler.adapter = adapter
    }

    @SuppressLint("WrongConstant")
    private fun courseCall(){
        //adding a layoutmanager
        courseRecycler.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        val adapter = CourseAdapter(courseList)

        //now adding the adapter to recyclerview
        courseRecycler.adapter = adapter
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
}
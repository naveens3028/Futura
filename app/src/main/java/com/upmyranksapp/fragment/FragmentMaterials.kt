package com.upmyranksapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upmyranksapp.R
import com.upmyranksapp.adapter.MaterialsPagerAdapter
import com.upmyranksapp.adapter.SubTopicsAdapter
import com.upmyranksapp.model.TopicResponseItem
import com.upmyranksapp.model.VideoMaterial
import com.upmyranksapp.model.chapter.TopicMaterialResponse
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_materials.*
import java.util.*

private const val ARG_PARAM1 = "param1"

class FragmentMaterials : Fragment() {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    var topicResponse: TopicMaterialResponse? = null
    lateinit var subTopicListAdapter: SubTopicsAdapter
    var chapterId = ""
    var batchId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_materials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)


    }


    override fun onResume() {
        super.onResume()
        arguments?.let {

            topicResponse = Gson().fromJson(
                it.getString(ARG_PARAM1),
                TopicMaterialResponse::class.java
                )

            subTopicTitle.text = topicResponse?.topic!!.courseName.toString()
        }

        if (!topicResponse?.materialList.isNullOrEmpty()) onTopicSelected(topicResponse?.materialList)
    }

     fun onTopicSelected(subTopicItems: List<VideoMaterial>?) {
        subTopicListAdapter = SubTopicsAdapter(requireContext(), subTopicItems!!)
        supTopicRecycler.adapter = subTopicListAdapter
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
        @JvmStatic
        fun newInstance(param1:TopicMaterialResponse) =
            FragmentMaterials().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, Gson().toJson(param1))
                }
            }
    }
}

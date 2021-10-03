package com.upmyranksapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.upmyranksapp.fragment.FragmentMaterials
import com.upmyranksapp.model.TopicResponse
import com.upmyranksapp.model.chapter.ChapterResponseData
import com.upmyranksapp.model.chapter.TopicMaterialResponse

class MaterialsPagerAdapter(fm: FragmentActivity, private val topic: List<TopicMaterialResponse>) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return topic.size
    }

    override fun createFragment(position: Int): Fragment {
        return FragmentMaterials.newInstance(topic[position])
    }
}
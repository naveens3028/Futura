package com.trisys.rn.baseapp.learn

import com.trisys.rn.baseapp.model.VideoMaterial

interface TopicClickListener {
    fun onTopicSelected(subTopicItems: List<VideoMaterial>)
}
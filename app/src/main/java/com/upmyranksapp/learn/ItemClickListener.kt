package com.upmyranksapp.learn

import com.upmyranksapp.model.VideoMaterial

interface TopicClickListener {
    fun onTopicSelected(subTopicItems: List<VideoMaterial>)
}
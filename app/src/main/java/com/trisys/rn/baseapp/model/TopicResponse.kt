package com.trisys.rn.baseapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class TopicResponse : ArrayList<TopicResponseItem>()

@Serializable
data class TopicResponseItem(
    @SerialName("materialList")
    val videoMaterialList: List<VideoMaterial>,
    @SerialName("topic")
    val topic: Topic
)

@Serializable
data class VideoMaterial(
    @SerialName("chatUrl")
    val chatUrl: String,
    /*@SerialName("coachingCenterBranchId")
    val coachingCenterBranchId: Any,
    @SerialName("coachingCenterBranchName")
    val coachingCenterBranchName: Any,
    @SerialName("coachingCenterId")
    val coachingCenterId: Any,
    @SerialName("coachingCenterName")
    val coachingCenterName: Any,*/
    @SerialName("courseId")
    val courseId: String,
    @SerialName("courseName")
    val courseName: String,
    @SerialName("createdAt")
    val createdAt: Long,
    /*@SerialName("createdBy")
    val createdBy: Any,*/
    @SerialName("description")
    val description: String,
    @SerialName("filePath")
    val filePath: String,
    @SerialName("id")
    val id: String,
    @SerialName("materialType")
    val materialType: String,
    @SerialName("orderSequence")
    val orderSequence: String,
    @SerialName("status")
    val status: String,
    @SerialName("title")
    val title: String,
    @SerialName("updatedAt")
    val updatedAt: Long,
    /*@SerialName("updatedBy")
    val updatedBy: Any*/
)

@Serializable
data class Topic(
    /*@SerialName("coachingCentre")
    val coachingCentre: Any,
    @SerialName("coachingCentreId")
    val coachingCentreId: Any,*/
    @SerialName("courseName")
    val courseName: String,
    @SerialName("createdAt")
    val createdAt: Long,
    /*@SerialName("createdBy")
    val createdBy: Any,*/
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: String,
    @SerialName("parentId")
    val parentId: String,
    /* @SerialName("parentName")
     val parentName: Any,*/
    @SerialName("status")
    val status: String,
    @SerialName("updatedAt")
    val updatedAt: Long,
    /*@SerialName("updatedBy")
    val updatedBy: Any*/
)
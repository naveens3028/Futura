package com.trisys.rn.baseapp.model

import com.google.gson.annotations.SerializedName


data class ScheduledTestClass(
    @SerializedName("MOCK_TEST")
    val mOCKTEST: List<MOCKTEST>,
    @SerializedName("PRACTICE")
    val pRACTICE: List<Any>
)

data class MOCKTEST(
    @SerializedName("batchIds")
    val batchIds: Any,
    @SerializedName("batchList")
    val batchList: Any,
    @SerializedName("branchIds")
    val branchIds: Any,
    @SerializedName("coachingCenterId")
    val coachingCenterId: String,
    @SerializedName("coachingCentre")
    val coachingCentre: CoachingCentre,
    @SerializedName("courseIds")
    val courseIds: Any,
    @SerializedName("createdAt")
    val createdAt: Long,
    @SerializedName("createdBy")
    val createdBy: Any,
    @SerializedName("expiryDate")
    val expiryDate: Any,
    @SerializedName("expiryDateTime")
    val expiryDateTime: Any,
    @SerializedName("expiryTime")
    val expiryTime: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("publishDate")
    val publishDate: Long,
    @SerializedName("publishDateTime")
    val publishDateTime: Long,
    @SerializedName("publishTime")
    val publishTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("testPaperId")
    val testPaperId: String,
    @SerializedName("testPaperVo")
    val testPaperVo: TestPaperVo,
    @SerializedName("testStatus")
    val testStatus: String,
    @SerializedName("updatedAt")
    val updatedAt: Long,
    @SerializedName("updatedBy")
    val updatedBy: Any
)

/*data class CoachingCentre1(
    @SerializedName("address1")
    val address1: String,
    @SerializedName("address2")
    val address2: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("coachingCenterCode")
    val coachingCenterCode: String,
    @SerializedName("coachingCentreName")
    val coachingCentreName: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("createdAt")
    val createdAt: Long,
    @SerializedName("createdBy")
    val createdBy: Any,
    @SerializedName("email")
    val email: String,
    @SerializedName("expiryOn")
    val expiryOn: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("logoUrl")
    val logoUrl: String,
    @SerializedName("mobileNumber")
    val mobileNumber: String,
    @SerializedName("questionLimit")
    val questionLimit: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedAt")
    val updatedAt: Long,
    @SerializedName("updatedBy")
    val updatedBy: Any,
    @SerializedName("zipCode")
    val zipCode: String
)*/

data class TestPaperVo(
    @SerializedName("attempts")
    val attempts: Int,
    @SerializedName("chapter")
    val chapter: Any,
    @SerializedName("chapterId")
    val chapterId: Any,
    @SerializedName("completionMessage")
    val completionMessage: Any,
    @SerializedName("correctMark")
    val correctMark: Int,
    @SerializedName("createdAt")
    val createdAt: Long,
    @SerializedName("createdBy")
    val createdBy: Any,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("instructions")
    val instructions: Any,
    @SerializedName("isHideAnsInResult")
    val isHideAnsInResult: Boolean,
    @SerializedName("isJumbling")
    val isJumbling: Boolean,
    @SerializedName("isPauseAllow")
    val isPauseAllow: Boolean,
    @SerializedName("isSolutionRequired")
    val isSolutionRequired: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("questionCount")
    val questionCount: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("testCode")
    val testCode: String,
    @SerializedName("testType")
    val testType: String,
    @SerializedName("timeLeft")
    val timeLeft: String,
    @SerializedName("unasweredMark")
    val unasweredMark: Int,
    @SerializedName("updatedAt")
    val updatedAt: Long,
    @SerializedName("updatedBy")
    val updatedBy: Any,
    @SerializedName("wrongMark")
    val wrongMark: Int
)
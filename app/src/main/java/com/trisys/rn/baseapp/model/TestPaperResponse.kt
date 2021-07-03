package com.trisys.rn.baseapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TestPaperResponse(
    @SerialName("quesionList")
    val quesionList: List<Quesion>,
    @SerialName("sectionList")
    val sectionList: List<Section>
)

@Serializable
data class Quesion(
    @SerialName("answer")
    val answer: String,
    @SerialName("answeredMark")
    val answeredMark: Int,
    @SerialName("id")
    val id: String,
    @SerialName("optionA")
    val optionA: String,
    @SerialName("optionB")
    val optionB: String,
    @SerialName("optionC")
    val optionC: String,
    @SerialName("optionD")
    val optionD: String,
    @SerialName("position")
    val position: Int,
    @SerialName("isAnswered")
    var isAnswered: Boolean = false,
    @SerialName("optionSelected")
    var optionSelected: String = "",
    @SerialName("questionContent")
    val questionContent: String,
    @SerialName("questionType")
    val questionType: String,
    @SerialName("unAnsweredMark")
    val unAnsweredMark: Int,
    @SerialName("wrongMark")
    val wrongMark: Int,
    @SerialName("timeSpent")
    val timeSpent: Long = 0
)

@Serializable
data class Section(
    @SerialName("sectionName")
    val sectionName: String,
    @SerialName("sectionQuesionList")
    val sectionQuesionList: List<SectionQuesion>
)

@Serializable
data class SectionQuesion(
    @SerialName("answer")
    val answer: String,
    @SerialName("answeredMark")
    val answeredMark: Int,
    @SerialName("id")
    val id: String,
    @SerialName("optionA")
    val optionA: String,
    @SerialName("optionB")
    val optionB: String,
    @SerialName("optionC")
    val optionC: String,
    @SerialName("optionD")
    val optionD: String,
    @SerialName("position")
    val position: Int,
    @SerialName("questionContent")
    val questionContent: String,
    @SerialName("questionType")
    val questionType: String,
    @SerialName("unAnsweredMark")
    val unAnsweredMark: Int,
    @SerialName("wrongMark")
    val wrongMark: Int
)

@Serializable
data class TestPaperForStudentResponse(
    @SerialName("data")
    val `data`: Data1
)

@Serializable
data class Data1(
    @SerialName("attemptedCount")
    val attemptedCount: Int,
    @SerialName("sectionList")
    val sectionList: List<Section1>,
    @SerialName("testPaper")
    val testPaper: TestPaper
)

@Serializable
data class Section1(
    @SerialName("questionCount")
    val questionCount: Int,
    @SerialName("sectionName")
    val sectionName: String
)

@Serializable
data class TestPaper(
    @SerialName("attempts")
    val attempts: Int,
    @SerialName("chapter")
    val chapter: String,
    @SerialName("chapterId")
    val chapterId: String,
    @SerialName("completionMessage")
    val completionMessage: String,
    @SerialName("correctMark")
    val correctMark: Int,
    @SerialName("createdAt")
    val createdAt: Long,
    @SerialName("createdBy")
    val createdBy: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("id")
    val id: String,
    @SerialName("instructions")
    val instructions: String,
    @SerialName("isHideAnsInResult")
    val isHideAnsInResult: Boolean,
    @SerialName("isJumbling")
    val isJumbling: Boolean,
    @SerialName("isPauseAllow")
    val isPauseAllow: Boolean,
    @SerialName("isSolutionRequired")
    val isSolutionRequired: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("questionCount")
    val questionCount: Int,
    @SerialName("status")
    val status: String,
    @SerialName("testCode")
    val testCode: String,
    @SerialName("testType")
    val testType: String,
    @SerialName("timeLeft")
    val timeLeft: Long,
    @SerialName("unasweredMark")
    val unasweredMark: Int,
    @SerialName("updatedAt")
    val updatedAt: Long,
    @SerialName("updatedBy")
    val updatedBy: Long,
    @SerialName("wrongMark")
    val wrongMark: Int
)
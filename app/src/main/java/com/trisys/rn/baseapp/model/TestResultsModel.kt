package com.trisys.rn.baseapp.model

import com.google.gson.annotations.SerializedName


data class TestResultsModel (
    @SerializedName("sectionsData")
    var sectionsData: List<SectionsDatum?>? = null,

    @SerializedName("totalTestAttempted")
    var totalTestAttempted: Int? = null,

    @SerializedName("totalCorrectMarks")
    var totalCorrectMarks: Int? = null,

    @SerializedName("totalCorrectAnsweredQuestion")
    var totalCorrectAnsweredQuestion: Int? = null,

    @SerializedName("totalWrongMarks")
    var totalWrongMarks: Int? = null,

    @SerializedName("totalUnattemptedMarks")
    var totalUnattemptedMarks: Int? = null,

    @SerializedName("totalMarks")
    var totalMarks: Int? = null,

    @SerializedName("totalAttemptedQuestions")
    var totalAttemptedQuestions: Int? = null,

    @SerializedName("totalWrongAttemptedQuestions")
    var totalWrongAttemptedQuestions: Int? = null,

    @SerializedName("totalUnAttemptedQuestons")
    var totalUnAttemptedQuestons: Int? = null,

    @SerializedName("totalObtainedMarks")
    var totalObtainedMarks: Int? = null,

    @SerializedName("totalQuestions")
    var totalQuestions: Int? = null,

    @SerializedName("accuracy")
    var accuracy: String? = null,

    @SerializedName("currentRank")
    var currentRank: Int? = null,

    @SerializedName("totalRank")
    var totalRank: Int? = null,

    @SerializedName("listTopRankers")
    var listTopRankers: List<ListTopRanker?>? = null,

    @SerializedName("pausedAt")
    var pausedAt: Any? = null,

    @SerializedName("totalConsumeTime")
    var totalConsumeTime: Int? = null,

    @SerializedName("testConsumeTimePercentage")
    var testConsumeTimePercentage: String? = null,

    @SerializedName("totalTimeTakenByTopper")
    var totalTimeTakenByTopper: Int? = null,

    @SerializedName("avgTimePerQuesByTopper")
    var avgTimePerQuesByTopper: Int? = null

)

class SectionsDatum (
    @SerializedName("sectionName")
    var sectionName: String? = null,

    @SerializedName("sectionQuestion")
    var sectionQuestion: List<SectionQuestion?>? = null,
)

class SectionQuestion (

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("position")
    var position: Int? = null,

    @SerializedName("question")
    var question: String? = null,

    @SerializedName("optionA")
    var optionA: String? = null,

    @SerializedName("optionB")
    var optionB: String? = null,

    @SerializedName("optionC")
    var optionC: String? = null,

    @SerializedName("optionD")
    var optionD: String? = null,

    @SerializedName("correctMarks")
    var correctMarks: Int? = null,

    @SerializedName("unAnsweredMarks")
    var unAnsweredMarks: Int? = null,

    @SerializedName("wrongMarks")
    var wrongMarks: Int? = null,

    @SerializedName("difficultyLevel")
    var difficultyLevel: String? = null,

    @SerializedName("correctAnswer")
    var correctAnswer: String? = null,

    @SerializedName("submittedAnswered")
    var submittedAnswered: String? = null,

    @SerializedName("explanation")
    var explanation: String? = null,

    @SerializedName("timeSpent")
    var timeSpent: String? = null,

    @SerializedName("timeSpentByTopper")
    var timeSpentByTopper: String? = null
)

class ListTopRanker (

    @SerializedName("studentName")
    var studentName: String? = null,

    @SerializedName("rank")
    var rank: Int? = null,

    @SerializedName("obtainedMarks")
    var obtainedMarks: Int? = null,

    @SerializedName("totalMarks")
    var totalMarks: Int? = null,

)



package com.trisys.rn.baseapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class ScheduledClass(
    var MOCK_TEST: List<MOCKTEST>,
    val PRACTICE: List<Any>
)

@Parcelize
data class MOCKTEST(
    val batchIds: String?,
    val batchList: String?,
    val branchIds: String?,
    val coachingCenterId: String,
    val coachingCentre: CoachingCentre1?,
    val courseIds: String?,
    val createdAt: Long,
    val createdBy: String?,
    val expiryDate: String?,
    val expiryDateTime: String?,
    val expiryTime: String?,
    val id: String,
    val publishDate: Long,
    val publishDateTime: Long,
    val publishTime: String?,
    val status: String,
    val testPaperId: String,
    val testPaperVo: TestPaperVo?,
    val testStatus: String,
    val updatedAt: Long,
    val updatedBy: String?
) : Parcelable

@Parcelize
data class CoachingCentre1(
    val address1: String,
    val address2: String,
    val city: String,
    val coachingCenterCode: String,
    val coachingCentreName: String,
    val country: String,
    val createdAt: Long,
    val createdBy: Long?,
    val email: String,
    val expiryOn: String,
    val id: String,
    val logoUrl: String,
    val mobileNumber: String,
    val questionLimit: String,
    val state: String,
    val status: String,
    val updatedAt: Long,
    val updatedBy: String?,
    val zipCode: String
) : Parcelable

@Entity(tableName = "test_paper")
@Parcelize
data class TestPaperVo(
    val attempts: Int,
    val chapter: String?,
    val chapterId: String?,
    val completionMessage: String?,
    val correctMark: Int,
    val createdAt: Long,
    val createdBy: String?,
    val duration: Int,
    @PrimaryKey
    val id: String,
    val instructions: String?,
    val isHideAnsInResult: Boolean,
    val isJumbling: Boolean,
    val isPauseAllow: Boolean,
    val isSolutionRequired: String?,
    val name: String,
    val questionCount: Int,
    val status: String,
    val testCode: String,
    val testType: String,
    val timeLeft: String,
    val unasweredMark: Int,
    val updatedAt: Long,
    val updatedBy: String?,
    val wrongMark: Int
) : Parcelable

@Entity
data class MergedTest(
    val batchIds: String?,
    val batchList: String?,
    val branchIds: String?,
    val coachingCenterId: String,
    val courseIds: String?,
    val createdAt: Long,
    val createdBy: String?,
    val expiryDate: String?,
    val expiryDateTime: String?,
    val expiryTime: String?,
    val id: String,
    val publishDate: Long,
    val publishDateTime: Long,
    val publishTime: String?,
    val status: String,
    val testPaperId: String,
    val testStatus: String,
    val updatedAt: Long,
    val updatedBy: String?,

    val attempts: Int,
    val chapter: String?,
    val chapterId: String?,
    val completionMessage: String?,
    val correctMark: Int,
    val duration: Int,
    val instructions: String?,
    val isHideAnsInResult: Boolean,
    val isJumbling: Boolean,
    val isPauseAllow: Boolean,
    val isSolutionRequired: String?,
    val name: String,
    val questionCount: Int,
    val testCode: String,
    val testType: String,
    val timeLeft: String,
    val unasweredMark: Int,
    val wrongMark: Int
)

data class SubmittedResult(
    val answerJson: String,
    val attempt: Int,
    val completeStatus: Any,
    val correctAnswered: Int,
    val correctMarks: Int,
    val createdAt: Long,
    val createdBy: Any,
    val id: String,
    val pausedAt: Any,
    val status: String,
    val studentId: String,
    val testDurationTime: String,
    val testPaperId: String,
    val totalMarks: Int,
    val totalQuesionPaperMarks: Int,
    val unAnswered: Int,
    val unAnsweredMarks: Int,
    val updatedAt: Long,
    val updatedBy: Any,
    val wrongAnswered: Int,
    val wrongAnsweredMarks: Int
)

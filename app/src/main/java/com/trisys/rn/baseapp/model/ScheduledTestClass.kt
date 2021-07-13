package com.trisys.rn.baseapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

data class ScheduledClass(
    val MOCK_TEST: List<MOCKTEST>,
    val PRACTICE: List<Any>
)

@Entity(tableName = "test_detail")
data class MOCKTEST(
    val batchIds: String?,
    val batchList: String?,
    val branchIds: String?,
    val coachingCenterId: String,
    @Ignore
    val coachingCentre: CoachingCentre1?,
    val courseIds: String?,
    val createdAt: Long,
    val createdBy: String?,
    val expiryDate: String?,
    val expiryDateTime: String?,
    val expiryTime: String?,
    @PrimaryKey
    val id: String,
    val publishDate: Long,
    val publishDateTime: Long,
    val publishTime: String?,
    val status: String,
    val testPaperId: String,
    @Ignore
    val testPaperVo: TestPaperVo?,
    val testStatus: String,
    val updatedAt: Long,
    val updatedBy: String?
) {
    constructor(
        batchIds: String?,
        batchList: String?,
        branchIds: String?,
        coachingCenterId: String,
        courseIds: String?,
        createdAt: Long,
        createdBy: String?,
        expiryDate: String?,
        expiryDateTime: String?,
        expiryTime: String?,
        id: String,
        publishDate: Long,
        publishDateTime: Long,
        publishTime: String?,
        status: String,
        testPaperId: String,
        testStatus: String,
        updatedAt: Long,
        updatedBy: String?
    ) : this(
        batchIds,
        batchList,
        branchIds,
        coachingCenterId,
        null,
        courseIds,
        createdAt,
        createdBy,
        expiryDate,
        expiryDateTime,
        expiryTime,
        id,
        publishDate,
        publishDateTime,
        publishTime,
        status,
        testPaperId,
        null,
        testStatus,
        updatedAt,
        updatedBy
    )
}

data class CoachingCentre1(
    val address1: String,
    val address2: String,
    val city: String,
    val coachingCenterCode: String,
    val coachingCentreName: String,
    val country: String,
    val createdAt: Long,
    val createdBy: Any,
    val email: String,
    val expiryOn: String,
    val id: String,
    val logoUrl: String,
    val mobileNumber: String,
    val questionLimit: String,
    val state: String,
    val status: String,
    val updatedAt: Long,
    val updatedBy: Any,
    val zipCode: String
)

@Entity(tableName = "test_paper")
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
)

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

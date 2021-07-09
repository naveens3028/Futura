package com.trisys.rn.baseapp.model.onBoarding

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName



data class LoginResponse(
    @SerializedName("data")var data:LoginData? = null,
)
data class LoginData(
    @SerializedName("role")var role:String? = null,
    @SerializedName("token")var token:String? = null,
    @SerializedName("userDetail")var userDetail:UserDetails? = null,
)
data class UserDetails(
    @SerializedName("userDetailId")var userDetailId:String? = null,
    @SerializedName("usersId")var usersId:String? = null,
    @SerializedName("password")var password:String? = null,
    @SerializedName("address1")var address1:String? = null,
    @SerializedName("address2")var address2:String? = null,
    @SerializedName("city")var city:String? = null,
    @SerializedName("country")var country:String? = null,
    @SerializedName("dob")var dob:String? = null,
    @SerializedName("email")var email:String? = null,
    @SerializedName("enrollmentNumber")var enrollmentNumber:String? = null,
    @SerializedName("fatherName")var fatherName:String? = null,
    @SerializedName("firstName")var firstName:String? = null,
    @SerializedName("lastName")var lastName:String? = "",
    @SerializedName("mobileNumber")var mobileNumber:String? = null,
    @SerializedName("profileImagePath")var profileImagePath:String? = null,
    @SerializedName("qualification")var qualification:String? = null,
    @SerializedName("salutation")var salutation:String? = null,
    @SerializedName("shortDiscription")var shortDiscription:String? = null,
    @SerializedName("state")var state:String? = null,
    @SerializedName("status")var status:String? = null,
    @SerializedName("updatedBy")var updatedBy:String? = null,
    @SerializedName("uploadFileId")var uploadFileId:String? = null,
    @SerializedName("userName")var userName:String? = null,
    @SerializedName("userType")var userType:String? = null,
    @SerializedName("yearOfExperience")var yearOfExperience:String? = null,
    @SerializedName("zipCode")var zipCode:String? = null,
    @SerializedName("roleId")var roleId:String? = null,
    @SerializedName("description")var description:String? = null,
    @SerializedName("deviceName")var deviceName:String? = null,
    @SerializedName("coachingCenterId")var coachingCenterId:String? = null,
    @SerializedName("studentAccess")var studentAccess:Boolean = false,
    @SerializedName("subject")var subject:String? = null,
    @SerializedName("coachingCentre")var coachingCentre:CoachingCentre? = null,
    @SerializedName("branchIds")var branchIds:ArrayList<String>? = null,
    @SerializedName("batchIds")var batchIds:ArrayList<String>? = null,
    @SerializedName("branchList")var branchList:ArrayList<branchItem>? = null,
    @SerializedName("batchList")var batchList:ArrayList<batchItem>,
)

data class CoachingCentre(
    @SerializedName("id")var id:String? = null,
    @SerializedName("coachingCentreName")var coachingCentreName:String? = null,
    @SerializedName("mobileNumber")var mobileNumber:String? = null,
    @SerializedName("email")var email:String? = null,
    @SerializedName("address1")var address1:String? = null,
    @SerializedName("address2")var address2:String? = null,
    @SerializedName("city")var city:String? = null,
    @SerializedName("country")var country:String? = null,
    @SerializedName("state")var state:String? = null,
    @SerializedName("zipCode")var zipCode:String? = null,
    @SerializedName("expiryOn")var expiryOn:String? = null,
    @SerializedName("status")var status:String? = null,
    @SerializedName("coachingCenterCode")var coachingCenterCode:String? = null,
    @SerializedName("questionLimit")var questionLimit:String? = null,
    @SerializedName("logoUrl")var logoUrl:String? = null,
)

data class branchItem(
    @SerializedName("id")var id:String? = null,
    @SerializedName("coachingCenterId")var coachingCenterId:String? = null,
    @SerializedName("branchName")var branchName:String? = null,
    @SerializedName("email")var email:String? = null,
    @SerializedName("address1")var address1:String? = null,
    @SerializedName("address2")var address2:String? = null,
    @SerializedName("city")var city:String? = null,
    @SerializedName("country")var country:String? = null,
    @SerializedName("state")var state:String? = null,
    @SerializedName("zipCode")var zipCode:String? = null,
    @SerializedName("mobileNumber")var mobileNumber:String? = null,
    @SerializedName("status")var status:String? = null,
    @SerializedName("isMainBranch")var isMainBranch:String? = null,
    @SerializedName("questionLimit")var questionLimit:String? = null,
    @SerializedName("courseIds")var courseIds:ArrayList<String>? = null,
    @SerializedName("webexUserIds")var webexUserIds:ArrayList<String>? = null,
    @SerializedName("webexUsers")var webexUsers:String? = null,
)
data class batchItem(
    @SerialName("additionalCourse")
    val additionalCourse: String,
    @SerialName("additionalCourseId")
    val additionalCourseId: String,
    @SerialName("batchEndDate")
    val batchEndDate: String,
    @SerialName("batchName")
    val batchName: String,
    @SerialName("batchSize")
    val batchSize: String,
    @SerialName("batchStartDate")
    val batchStartDate: String,
    @SerialName("coachingCenterBranchId")
    val coachingCenterBranchId: String,
    @SerialName("coachingCenterId")
    val coachingCenterId: String,
    @SerialName("coachingCentre")
    val coachingCentre: CoachingCentre,
    @SerialName("course")
    val course: Course,
    @SerialName("courseId")
    val courseId: String,
    @SerialName("createdAt")
    val createdAt: Long,
    @SerialName("createdBy")
    val createdBy: String,
    @SerialName("description")
    val description: String,
    @SerialName("endTiming")
    val endTiming: String,
    @SerialName("id")
    val id: String,
    @SerialName("startTiming")
    val startTiming: String,
    @SerialName("status")
    val status: String,
    @SerialName("updatedAt")
    val updatedAt: Long,
    @SerialName("updatedBy")
    val updatedBy: String
)

data class Course(
    @SerializedName("id")var id:String? = null,
    @SerializedName("courseName")var courseName:String? = null,
    @SerializedName("parentId")var parentId:String? = null,
    @SerializedName("parentName")var parentName:String? = null,
    @SerializedName("description")var description:String? = null,
    @SerializedName("status")var status:String? = null,
    @SerializedName("coachingCentre")var coachingCentre:CoachingCentre? = null,
    @SerializedName("coachingCentreId")var coachingCentreId:String? = null,
)

data class AverageBatchTests (
    @SerializedName("studentAverage") var studentAverage: Double? = null,
    @SerializedName("classAverage") var classAverage: Double? = null,
    @SerializedName("rank") var rank: Int? = null,
    @SerializedName("total_students") var totalStudents: Int? = null,
    @SerializedName("total_test") var totalTest: Int? = null,
    @SerializedName("student_test") var studentTest: Int? = null,
    @SerializedName("topperAverage") var topperAverage: Double? = null,
)

data class UnAttempted (
    @SerializedName("MOCK_TEST") var mockTest: List<MockTest>? = null,
    @SerializedName("PRACTICE") var practice: List<Any>? = null
)

data class MockTest (
    @SerializedName("publishDate") var publishDate: Long? = null,
    @SerializedName("publishTime") var publishTime: String? = null,
    @SerializedName("publishDateTime") var publishDateTime: Long? = null,
    @SerializedName("expiryDate") var expiryDate: Any? = null,
    @SerializedName("expiryTime") var expiryTime: Any? = null,
    @SerializedName("expiryDateTime") var expiryDateTime: Any? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("questionCount") var questionCount: Int? = null,
    @SerializedName("totalAttempts") var totalAttempts: Int? = null,
    @SerializedName("testCode") var testCode: String? = null,
    @SerializedName("testType") var testType: String? = null,
    @SerializedName("studentAnswerId") var studentAnswerId: String? = null,
    @SerializedName("completeStatus") var completeStatus: Any? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("studentId") var studentId: String? = null,
    @SerializedName("testPaperId") var testPaperId: String? = null,
    @SerializedName("correctMarks") var correctMarks: Any? = null,
    @SerializedName("studentName") var studentName: Any? = null,
    @SerializedName("totalDuration") var totalDuration: Any? = null,
    @SerializedName("totalMarks") var totalMarks: Any? = null,
)

data class AttemptedResponse(
    @SerializedName("MOCK_TEST")
    val mOCKTEST: List<AttemptedTest>,
    @SerializedName("PRACTICE")
    val pRACTICE: List<Any>
)

data class AttemptedTest(
    @SerializedName("completeStatus")
    val completeStatus: String,
    @SerializedName("correctMarks")
    val correctMarks: Int = 1,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("expiryDate")
    val expiryDate: Any,
    @SerializedName("expiryDateTime")
    val expiryDateTime: Any,
    @SerializedName("expiryTime")
    val expiryTime: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("publishDate")
    val publishDate: Long,
    @SerializedName("publishDateTime")
    val publishDateTime: Long,
    @SerializedName("publishTime")
    val publishTime: String,
    @SerializedName("questionCount")
    val questionCount: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("studentAnswerId")
    val studentAnswerId: String,
    @SerializedName("studentId")
    val studentId: String,
    @SerializedName("studentName")
    val studentName: Any,
    @SerializedName("testCode")
    val testCode: String,
    @SerializedName("testPaperId")
    val testPaperId: String,
    @SerializedName("testType")
    val testType: String,
    @SerializedName("totalAttempts")
    val totalAttempts: Int,
    @SerializedName("totalDuration")
    val totalDuration: Any,
    @SerializedName("totalMarks")
    val totalMarks: Any
)

@Serializable
data class TestStatusResponse(
    @SerialName("data")
    val `data`: String
)
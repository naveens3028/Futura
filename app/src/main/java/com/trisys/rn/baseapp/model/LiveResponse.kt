package com.trisys.rn.baseapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LiveResponse(
    @SerializedName("data") val `data`: List<Data>,
    @SerializedName("page") val page: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("totalPages") val totalPages: Int
)

data class Data(
    @SerializedName("batchIds") val batchIds: List<String>,
    @SerializedName("batchList") val batchList: List<Batch>,
    @SerializedName("branchIds") val branchIds: List<String>,
    @SerializedName("branchList") val branchList: List<Branch>,
    @SerializedName("coachingCentre") val coachingCentre: CoachingCentreXX,
    @SerializedName("coachingCentreId") val coachingCentreId: String,
    @SerializedName("course") val course: CourseX,
    @SerializedName("courseId") val courseId: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("endDateTime") val endDateTime: Long,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("id") val id: String,
    @SerializedName("platform") val platform: String,
    @SerializedName("sessionDate") val sessionDate: Long,
    @SerializedName("sessionName") val sessionName: String,
    @SerializedName("sessionRecordingUrl") val sessionRecordingUrl: Any,
    @SerializedName("sessionType") val sessionType: String,
    @SerializedName("startDateTime") val startDateTime: Long,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("subject") val subject: Subject,
    @SerializedName("subjectId") val subjectId: String,
    @SerializedName("teacherId") val teacherId: String,
    @SerializedName("teacherUrl") val teacherUrl: String,
    @SerializedName("topicName") val topicName: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("url") val url: String,
    @SerializedName("userDetail") val userDetail: UserDetail,
    @SerializedName("webExUserId") val webExUserId: Any,
    @SerializedName("webexMeetingId") val webexMeetingId: Any,
    @SerializedName("webexSessionKey") val webexSessionKey: Any,
    @SerializedName("webexSessionPass") val webexSessionPass: Any,
    @SerializedName("webexUser") val webexUser: Any,
    @SerializedName("zoomUser") val zoomUser: ZoomUser,
    @SerializedName("zoomUserId") val zoomUserId: String
)

data class Batch(
    @SerializedName("additionalCourse") val additionalCourse: Any,
    @SerializedName("additionalCourseId") val additionalCourseId: Any,
    @SerializedName("batchEndDate") val batchEndDate: String,
    @SerializedName("batchName") val batchName: String,
    @SerializedName("batchSize") val batchSize: String,
    @SerializedName("batchStartDate") val batchStartDate: String,
    @SerializedName("coachingCenterBranchId") val coachingCenterBranchId: String,
    @SerializedName("coachingCenterId") val coachingCenterId: String,
    @SerializedName("coachingCentre") val coachingCentre: CoachingCentre,
    @SerializedName("coachingCentreBranch") val coachingCentreBranch: CoachingCentreBranch,
    @SerializedName("course") val course: Course,
    @SerializedName("courseId") val courseId: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("description") val description: String,
    @SerializedName("endTiming") val endTiming: String,
    @SerializedName("id") val id: String,
    @SerializedName("startTiming") val startTiming: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any
)

data class Branch(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("branchName") val branchName: String,
    @SerializedName("city") val city: String,
    @SerializedName("coachingCenterId") val coachingCenterId: String,
    @SerializedName("country") val country: String,
    @SerializedName("courseIds") val courseIds: Any,
    @SerializedName("courseList") val courseList: Any,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: String,
    @SerializedName("isMainBranch") val isMainBranch: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("questionLimit") val questionLimit: String,
    @SerializedName("state") val state: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("webexUserIds") val webexUserIds: Any,
    @SerializedName("webexUsers") val webexUsers: Any,
    @SerializedName("zipCode") val zipCode: String
)

data class CoachingCentreXX(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("city") val city: String,
    @SerializedName("coachingCenterCode") val coachingCenterCode: String,
    @SerializedName("coachingCentreName") val coachingCentreName: String,
    @SerializedName("country") val country: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("email") val email: String,
    @SerializedName("expiryOn") val expiryOn: String,
    @SerializedName("id") val id: String,
    @SerializedName("logoUrl") val logoUrl: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("questionLimit") val questionLimit: String,
    @SerializedName("state") val state: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("zipCode") val zipCode: String
)

data class CourseX(
    @SerializedName("coachingCentre") val coachingCentre: CoachingCentreXXX,
    @SerializedName("coachingCentreId") val coachingCentreId: String,
    @SerializedName("courseName") val courseName: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("parentId") val parentId: Any,
    @SerializedName("parentName") val parentName: Any,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any
)

data class Subject(
    @SerializedName("coachingCentre") val coachingCentre: Any,
    @SerializedName("coachingCentreId") val coachingCentreId: Any,
    @SerializedName("courseName") val courseName: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("parentId") val parentId: String,
    @SerializedName("parentName") val parentName: Any,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any
)

data class UserDetail(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("batchIds") val batchIds: Any,
    @SerializedName("batchList") val batchList: Any,
    @SerializedName("branchIds") val branchIds: Any,
    @SerializedName("branchList") val branchList: Any,
    @SerializedName("city") val city: String,
    @SerializedName("coachingCenterId") val coachingCenterId: String,
    @SerializedName("coachingCentre") val coachingCentre: CoachingCentreXXXX,
    @SerializedName("country") val country: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("email") val email: String,
    @SerializedName("enrollmentNumber") val enrollmentNumber: String,
    @SerializedName("fatherName") val fatherName: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("id") val id: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("profileImagePath") val profileImagePath: Any,
    @SerializedName("qualification") val qualification: String,
    @SerializedName("salutation") val salutation: String,
    @SerializedName("shortDiscription") val shortDiscription: Any,
    @SerializedName("state") val state: String,
    @SerializedName("status") val status: String,
    @SerializedName("studentAccess") val studentAccess: Boolean,
    @SerializedName("subject") val subject: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("uploadFileId") val uploadFileId: Any,
    @SerializedName("user") val user: User,
    @SerializedName("userId") val userId: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("yearOfExperience") val yearOfExperience: String,
    @SerializedName("zipCode") val zipCode: String
)

data class ZoomUser(
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("id") val id: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("zoomUser") val zoomUser: String
)

data class CoachingCentre(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("city") val city: String,
    @SerializedName("coachingCenterCode") val coachingCenterCode: String,
    @SerializedName("coachingCentreName") val coachingCentreName: String,
    @SerializedName("country") val country: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("email") val email: String,
    @SerializedName("expiryOn") val expiryOn: String,
    @SerializedName("id") val id: String,
    @SerializedName("logoUrl") val logoUrl: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("questionLimit") val questionLimit: String,
    @SerializedName("state") val state: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("zipCode") val zipCode: String
)

data class CoachingCentreBranch(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("branchName") val branchName: String,
    @SerializedName("city") val city: String,
    @SerializedName("coachingCenterId") val coachingCenterId: String,
    @SerializedName("country") val country: String,
    @SerializedName("courseIds") val courseIds: Any,
    @SerializedName("courseList") val courseList: Any,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: String,
    @SerializedName("isMainBranch") val isMainBranch: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("questionLimit") val questionLimit: String,
    @SerializedName("state") val state: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("webexUserIds") val webexUserIds: Any,
    @SerializedName("webexUsers") val webexUsers: Any,
    @SerializedName("zipCode") val zipCode: String
)

data class Course(
    @SerializedName("coachingCentre") val coachingCentre: CoachingCentreX,
    @SerializedName("coachingCentreId") val coachingCentreId: String,
    @SerializedName("courseName") val courseName: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("parentId") val parentId: Any,
    @SerializedName("parentName") val parentName: Any,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any
)

data class CoachingCentreX(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("city") val city: String,
    @SerializedName("coachingCenterCode") val coachingCenterCode: String,
    @SerializedName("coachingCentreName") val coachingCentreName: String,
    @SerializedName("country") val country: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("email") val email: String,
    @SerializedName("expiryOn") val expiryOn: String,
    @SerializedName("id") val id: String,
    @SerializedName("logoUrl") val logoUrl: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("questionLimit") val questionLimit: String,
    @SerializedName("state") val state: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("zipCode") val zipCode: String
)

data class CoachingCentreXXX(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("city") val city: String,
    @SerializedName("coachingCenterCode") val coachingCenterCode: String,
    @SerializedName("coachingCentreName") val coachingCentreName: String,
    @SerializedName("country") val country: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("email") val email: String,
    @SerializedName("expiryOn") val expiryOn: String,
    @SerializedName("id") val id: String,
    @SerializedName("logoUrl") val logoUrl: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("questionLimit") val questionLimit: String,
    @SerializedName("state") val state: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("zipCode") val zipCode: String
)

data class CoachingCentreXXXX(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("city") val city: String,
    @SerializedName("coachingCenterCode") val coachingCenterCode: String,
    @SerializedName("coachingCentreName") val coachingCentreName: String,
    @SerializedName("country") val country: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: Any,
    @SerializedName("email") val email: String,
    @SerializedName("expiryOn") val expiryOn: String,
    @SerializedName("id") val id: String,
    @SerializedName("logoUrl") val logoUrl: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("questionLimit") val questionLimit: String,
    @SerializedName("state") val state: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("zipCode") val zipCode: String
)

data class User(
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("id") val id: String,
    @SerializedName("loginDevice") val loginDevice: String,
    @SerializedName("newPassword") val newPassword: Any,
    @SerializedName("password") val password: String,
    @SerializedName("status") val status: String,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("updatedBy") val updatedBy: Any,
    @SerializedName("userName") val userName: String
)

class TestResultsData {
    @SerializedName("testName") var testName: String? = null
    @SerializedName("testType") var testType: String? = null
    @SerializedName("score") var score: Int? = null
    @SerializedName("highestScore") var highestScore: Int? = null
    @SerializedName("rankInTest") var rankInTest: Int? = null
    @SerializedName("attempt") var attempt: String? = null
    @SerializedName("testSubmissionCreatedDate") var testSubmissionCreatedDate: Long? = null
    @SerializedName("testSubmissionUpdatedDate") var testSubmissionUpdatedDate: Long? = null
    @SerializedName("testPaperId") var testPaperId: String? = null
}
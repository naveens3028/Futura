package com.trisys.rn.baseapp.model.onBoarding

import com.google.gson.annotations.SerializedName
import java.util.*

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
    @SerializedName("batchList")var batchList:ArrayList<batchItem>? = null,
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
    @SerializedName("id")var id:String? = null,
    @SerializedName("batchName")var batchName:String? = null,
    @SerializedName("branchName")var branchName:String? = null,
    @SerializedName("coachingCentre")var coachingCentre:CoachingCentre? = null,
    @SerializedName("coachingCenterId")var coachingCenterId:String? = null,
    @SerializedName("courseId")var courseId:String? = null,
    @SerializedName("coachingCentreBranch")var coachingCentreBranch:branchItem? = null,
    @SerializedName("coachingCenterBranchId")var coachingCenterBranchId:String? = null,
    @SerializedName("batchStartDate")var batchStartDate:String? = null,
    @SerializedName("batchEndDate")var batchEndDate:String? = null,
    @SerializedName("startTiming")var startTiming:String? = null,
    @SerializedName("endTiming")var endTiming:String? = null,
    @SerializedName("batchSize")var batchSize:String? = null,
    @SerializedName("description")var description:String? = null,
    @SerializedName("status")var status:String? = null,
    @SerializedName("additionalCourseId")var additionalCourseId:ArrayList<String>? = null,
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

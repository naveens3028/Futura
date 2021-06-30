package com.trisys.rn.baseapp.onBoarding.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidnetworking.common.Priority
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.trisys.rn.baseapp.MainActivity
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.onBoarding.LoginResponse
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginFragment : Fragment(), OnNetworkResponse {

    lateinit var mRemoteConfig: FirebaseRemoteConfig
    lateinit var networkHelper: NetworkHelper

    //Google Auth
    private lateinit var auth: FirebaseAuth

    //Google signin client
    private lateinit var googleSignInClient: GoogleSignInClient

    //preference class
    lateinit var myPreferences: MyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = MyPreferences(requireContext())
        mRemoteConfig = Firebase.remoteConfig
        networkHelper = NetworkHelper(requireContext())
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = Define.REMOTE_CONFIG_MINIMUM_INTERVAL
        }
        mRemoteConfig.setConfigSettingsAsync(configSettings)
        mRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        mRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("LoginFragment", "Config params updated: $updated")
                    Toast.makeText(
                        requireContext(), "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(), "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        continueButton.setOnClickListener {
            requestLogin()
            /*val res =
                "{\"data\":{\"role\":\"STUDENT\",\"userDetail\":{\"userDetailId\":\"3bbfc352-a8dc-44dc-a03b-f438e355c650\",\"usersId\":\"ade8e7e9-7c04-46ff-b70f-ea64a97e8f96\",\"password\":null,\"address1\":\"5/42, 1st floor, Labour colony, 3rd street, Guindy Industrial Estate,\",\"address2\":\"\",\"city\":\"Chennai\",\"country\":\"India\",\"createdAt\":1623258995000,\"createdBy\":\"503a8a58-d747-4937-923e-b40cff466aa1\",\"dob\":\"1986-08-24\",\"email\":\"dinaharan.s@gmail.com\",\"enrollmentNumber\":\"\",\"fatherName\":\"Siva\",\"firstName\":\"testDina\",\"lastName\":\"Dina\",\"mobileNumber\":\"8563245698\",\"profileImagePath\":null,\"qualification\":\"\",\"salutation\":\"Mr.\",\"shortDiscription\":null,\"state\":\"Tamil Nadu\",\"status\":\"ACTIVE\",\"updatedBy\":null,\"uploadFileId\":null,\"userName\":\"teststudent\",\"userType\":\"STUDENT\",\"yearOfExperience\":\"\",\"zipCode\":\"600040\",\"roleId\":null,\"description\":null,\"deviceName\":null,\"coachingCentre\":{\"id\":\"85075500-8044-492e-a590-f7ca04670cce\",\"coachingCentreName\":\"UpMyRanks\",\"mobileNumber\":\"1234567890\",\"email\":\"\",\"address1\":\"bang\",\"address2\":\"bang\",\"country\":\"India\",\"state\":\"Karnataka\",\"city\":\"bang\",\"zipCode\":\"560001\",\"expiryOn\":\"2024-01-31\",\"status\":\"ACTIVE\",\"coachingCenterCode\":\"CCD\",\"questionLimit\":\"10000000\",\"logoUrl\":\"https://eduinstitute.s3.ap-south-1.amazonaws.com/Activity_Files/StudentActivity_1588006052427.png\",\"createdAt\":1605676385000,\"updatedAt\":1605676385000,\"createdBy\":null,\"updatedBy\":null},\"coachingCenterId\":\"85075500-8044-492e-a590-f7ca04670cce\",\"studentAccess\":false,\"subject\":\"\",\"branchIds\":[\"bfd13c0e-6e83-4387-915e-4bd0c3d1dc8c\"],\"batchIds\":[\"23628f56-8128-498c-8ec8-2e6cffb4b22b\"],\"branchList\":[{\"id\":\"bfd13c0e-6e83-4387-915e-4bd0c3d1dc8c\",\"coachingCenterId\":\"85075500-8044-492e-a590-f7ca04670cce\",\"branchName\":\"BEST School\",\"address1\":\"banglore\",\"address2\":\"bavg\",\"country\":\"india\",\"state\":\"kar\",\"city\":\"bang\",\"zipCode\":\"123456\",\"mobileNumber\":\"1234567890\",\"email\":\"best@gmail.com\",\"isMainBranch\":\"YES\",\"questionLimit\":\"1000000\",\"status\":\"ACTIVE\",\"createdAt\":1610694241000,\"updatedAt\":1610694241000,\"createdBy\":null,\"updatedBy\":null,\"courseIds\":null,\"courseList\":null,\"webexUserIds\":null,\"webexUsers\":null}],\"batchList\":[{\"id\":\"23628f56-8128-498c-8ec8-2e6cffb4b22b\",\"batchName\":\"BEST Grade 9\",\"coachingCentre\":{\"id\":\"85075500-8044-492e-a590-f7ca04670cce\",\"coachingCentreName\":\"UpMyRanks\",\"mobileNumber\":\"1234567890\",\"email\":\"\",\"address1\":\"bang\",\"address2\":\"bang\",\"country\":\"India\",\"state\":\"Karnataka\",\"city\":\"bang\",\"zipCode\":\"560001\",\"expiryOn\":\"2024-01-31\",\"status\":\"ACTIVE\",\"coachingCenterCode\":\"CCD\",\"questionLimit\":\"10000000\",\"logoUrl\":\"https://eduinstitute.s3.ap-south-1.amazonaws.com/Activity_Files/StudentActivity_1588006052427.png\",\"createdAt\":1605676385000,\"updatedAt\":1605676385000,\"createdBy\":null,\"updatedBy\":null},\"coachingCenterId\":\"85075500-8044-492e-a590-f7ca04670cce\",\"course\":{\"id\":\"20c3a01e-3e4c-4f13-b88a-529f61a2d30a\",\"courseName\":\"Grade 9\",\"parentId\":null,\"parentName\":null,\"description\":\"course\",\"status\":\"ACTIVE\",\"coachingCentre\":{\"id\":\"85075500-8044-492e-a590-f7ca04670cce\",\"coachingCentreName\":\"UpMyRanks\",\"mobileNumber\":\"1234567890\",\"email\":\"\",\"address1\":\"bang\",\"address2\":\"bang\",\"country\":\"India\",\"state\":\"Karnataka\",\"city\":\"bang\",\"zipCode\":\"560001\",\"expiryOn\":\"2024-01-31\",\"status\":\"ACTIVE\",\"coachingCenterCode\":\"CCD\",\"questionLimit\":\"10000000\",\"logoUrl\":\"https://eduinstitute.s3.ap-south-1.amazonaws.com/Activity_Files/StudentActivity_1588006052427.png\",\"createdAt\":1605676385000,\"updatedAt\":1605676385000,\"createdBy\":null,\"updatedBy\":null},\"coachingCentreId\":\"85075500-8044-492e-a590-f7ca04670cce\",\"createdAt\":1607172239000,\"updatedAt\":1607172239000,\"createdBy\":null,\"updatedBy\":null},\"courseId\":\"20c3a01e-3e4c-4f13-b88a-529f61a2d30a\",\"coachingCentreBranch\":{\"id\":\"bfd13c0e-6e83-4387-915e-4bd0c3d1dc8c\",\"coachingCenterId\":\"85075500-8044-492e-a590-f7ca04670cce\",\"branchName\":\"BEST School\",\"address1\":\"banglore\",\"address2\":\"bavg\",\"country\":\"india\",\"state\":\"kar\",\"city\":\"bang\",\"zipCode\":\"123456\",\"mobileNumber\":\"1234567890\",\"email\":\"best@gmail.com\",\"isMainBranch\":\"YES\",\"questionLimit\":\"1000000\",\"status\":\"ACTIVE\",\"createdAt\":1610694241000,\"updatedAt\":1610694241000,\"createdBy\":null,\"updatedBy\":null,\"courseIds\":null,\"courseList\":null,\"webexUserIds\":null,\"webexUsers\":null},\"coachingCenterBranchId\":\"bfd13c0e-6e83-4387-915e-4bd0c3d1dc8c\",\"batchStartDate\":\"2021-01-15\",\"batchEndDate\":\"2021-01-15\",\"startTiming\":\"13:07\",\"endTiming\":\"13:07\",\"batchSize\":\"50000\",\"description\":\"grade 9\",\"status\":\"ACTIVE\",\"additionalCourseId\":null,\"additionalCourse\":null,\"createdAt\":1610696288000,\"updatedAt\":1610696288000,\"createdBy\":null,\"updatedBy\":null}]},\"token\":\"a212dde8-4a8d-4a80-8ec4-432dc8e5c469\"}}"
            loginResponseData(res)*/
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.container, OTPFragment()).addToBackStack(null)
//                .commitAllowingStateLoss()
        }
        google.setOnClickListener {
            signIn()
        }
        facebook.setOnClickListener {
            auth.signOut()
        }
    }

    private fun requestLogin() {
        val username = emailAddress.text.toString()
        val password = mobileNumber.text.toString()

        if (username.length == 0) {
            emailAddress.error = "Enter valid username"
        } else if (password.length == 0) {
            mobileNumber.error = "Enter valid password"
        } else {
            emailAddress.error = null
            mobileNumber.error = null

            val params = HashMap<String, String>()
            params.put("loginDevice", "mobile")
            params.put("userName", username)
            params.put("password", password)

            val jsonObject = JSONObject()
            try {
                jsonObject.put("loginDevice", "mobile")
                jsonObject.put("userName", username)
                jsonObject.put("password", password)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            requireActivity().stateful.showProgress()
            requireActivity().stateful.setProgressText("Loading..")

            networkHelper.loginPostCall(
                "https://api.upmyranks.com/app/api/v1/auth",
                params,
                Priority.HIGH,
                "login",
                this
            )
        }
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        requireActivity().stateful.showContent()
        if (responseCode == networkHelper.responseSuccess && tag.equals("login")) {

            loginResponseData(response)
        } else {
            Toast.makeText(requireContext(), "login Failed", Toast.LENGTH_LONG).show()
        }
    }

    fun loginResponseData(response: String) {
        val loginResponse = Gson().fromJson(response, LoginResponse::class.java)
        if (loginResponse.data != null) {
            Toast.makeText(requireContext(), "login successful", Toast.LENGTH_LONG).show()
            myPreferences.setString(Define.ACCESS_TOKEN, loginResponse.data!!.token)
            myPreferences.setString(Define.LOGIN_DATA, Gson().toJson(loginResponse.data))

            //Go to dashboard screen
            requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
        } else {
            Toast.makeText(requireContext(), "Login failed, please try again", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Log.d("USERNAME", user!!.displayName!!)
            Log.d("EMAIL", user!!.email!!)
            Log.d("PHONE NUMBER", "" + user!!.phoneNumber)
            Log.d("PHOTO URL", "" + user!!.photoUrl.toString())
            Log.d("UID", "" + user!!.uid)
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}
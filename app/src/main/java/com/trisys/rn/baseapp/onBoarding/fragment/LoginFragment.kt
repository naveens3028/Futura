package com.trisys.rn.baseapp.onBoarding.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import com.trisys.rn.baseapp.network.Config
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.RequestType
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.*


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

            requireActivity().stateful.showProgress()
            requireActivity().stateful.setProgressText("Loading..")
            val url =
                mRemoteConfig.getString(Define.BASE_URL) + mRemoteConfig.getString(Define.BASE_PATH) + Config.Login
            networkHelper.call(RequestType.POST_WITHOUT_AUTH, url, params, "login", this)
        }
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        requireActivity().stateful.showContent()
        if (responseCode == networkHelper.responseSuccess && tag.equals("login")) {
            Toast.makeText(requireContext(), "login successful", Toast.LENGTH_LONG).show()
            loginResponseData(response)
        } else {
            Toast.makeText(requireContext(), "login Failed", Toast.LENGTH_LONG).show()
        }
    }

    fun loginResponseData(response: String) {
        val loginResponse = Gson().fromJson(response, LoginResponse::class.java)
        if (loginResponse.data != null) {

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
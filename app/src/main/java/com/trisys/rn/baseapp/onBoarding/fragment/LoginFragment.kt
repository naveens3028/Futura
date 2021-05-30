package com.trisys.rn.baseapp.onBoarding.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidnetworking.common.Priority
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.network.Config
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.utils.Define
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), OnNetworkResponse {

    lateinit var mRemoteConfig: FirebaseRemoteConfig
    lateinit var networkHelper: NetworkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRemoteConfig = Firebase.remoteConfig
        networkHelper = NetworkHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onStart() {
        super.onStart()
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
           // requestLogin()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.container, OTPFragment())?.addToBackStack(null)
                ?.commitAllowingStateLoss()
        }
    }

    private fun requestLogin() {
        val params = HashMap<String, String>()
        params.put("loginDevice", "mobile")
        params.put("userName", "qr1001")
        params.put("password", "upmyranks123")

        val url =
            mRemoteConfig.getString(Define.BASE_URL) + mRemoteConfig.getString(Define.BASE_PATH) + Config.Login
        networkHelper.call(networkHelper.POST, url, params, Priority.HIGH, "login", this)

    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag.equals("login")) {
            Toast.makeText(requireContext(), "login successful", Toast.LENGTH_LONG).show()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, OTPFragment()).addToBackStack(null)
                .commitAllowingStateLoss()
        } else {
            Toast.makeText(requireContext(), "login successful", Toast.LENGTH_LONG).show()
        }
    }

}
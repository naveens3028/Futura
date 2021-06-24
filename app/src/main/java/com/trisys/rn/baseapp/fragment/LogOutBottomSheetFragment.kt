package com.trisys.rn.baseapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidnetworking.common.Priority
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.onBoarding.LoginActivity
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.URLHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog_logout.*

class LogOutBottomSheetFragment : BottomSheetDialogFragment(), OnNetworkResponse {

    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton.setOnClickListener {
            dismiss()
        }
        yesButton.setOnClickListener {
            logoutRequest()
        }
    }

    private fun logoutRequest() {

        val params = HashMap<String, String>()
        params["id"] = myPreferences.getString(Define.ACCESS_TOKEN).toString()

        requireActivity().stateful.showProgress()
        requireActivity().stateful.setProgressText("Loading...")
        networkHelper.call(
            networkHelper.POST,
            URLHelper.logout,
            params,
            Priority.HIGH,
            "logout",
            this
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        requireActivity().stateful.showContent()
        if (responseCode == networkHelper.responseSuccess && tag == "logout") {
            Toast.makeText(requireContext(), "logout successful", Toast.LENGTH_LONG).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            requireContext().startActivity(intent)
            myPreferences.setString(Define.LOGIN_DATA, "")
            activity?.finishAffinity()

        } else {
            Toast.makeText(
                requireContext(),
                "logout Failed...Please try sometimes later",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
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
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.onBoarding.LoginActivity
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog_logout.*
import kotlinx.android.synthetic.main.row_study.*

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
            clearUserData()
        }
    }

    fun clearUserData(){
        myPreferences.clearAllData()
    }

    private fun logoutRequest() {
        yesButton.visibility = View.INVISIBLE
//        requireActivity().progressBar.visibility = View.VISIBLE
        val params = HashMap<String, String>()
        params["id"] = myPreferences.getString(Define.ACCESS_TOKEN).toString()

        networkHelper.call(
            networkHelper.POST,
            networkHelper.RESTYPE_OBJECT,
            URLHelper.logout,
            params,
            Priority.HIGH,
            "logout",
            this
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "logout") {
            Toast.makeText(requireContext(), "logout successful", Toast.LENGTH_LONG).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            requireContext().startActivity(intent)
            myPreferences.setString(Define.LOGIN_DATA, "")
            activity?.finishAffinity()

        } else {
            yesButton.visibility = View.VISIBLE
//            progressBar.visibility = View.GONE
            Toast.makeText(
                requireContext(),
                "logout Failed...Please try sometimes later",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.login.CourseSelectionActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit


class RegistrationActivity: AppCompatActivity() {

    private val myList = ArrayList<String>()
    lateinit var mAuth: FirebaseAuth

    // string for storing our verification ID
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        // The test phone number and code should be whitelisted in the console.
        val phoneNumber = "+919790733050"
        val smsCode = "000000"

        mAuth = Firebase.auth
        val firebaseAuthSettings = mAuth.firebaseAuthSettings

        myList.apply {
            this.add("Student")
            this.add("Institute")
        }

        val newList: List<String> = myList

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.usertype,
            R.layout.spinnerview
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserType.adapter = adapter

        spinnerUserType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                Toast.makeText(this@RegistrationActivity, newList[position],Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        sbtbtn.setOnClickListener {
            // Configure faking the auto-retrieval with the whitelisted numbers.

        /*    // Turn off phone auth app verification.
            mAuth.getFirebaseAuthSettings()
                .setAppVerificationDisabledForTesting(true);*/
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onCodeSent(
                        verificationId: String,
                        forceResendingToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        // Save the verification id somewhere
                        // ...

                        // The corresponding whitelisted code above should be used to complete sign-in.
                       // this.enableUserManuallyInputCode()
                    }

                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        // Sign in with the credential
                        // ...

                        Toast.makeText(this@RegistrationActivity,"success", Toast.LENGTH_LONG).show()
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // ...
                        Toast.makeText(this@RegistrationActivity,e.toString(), Toast.LENGTH_LONG).show()
                        Log.e("popsi", e.toString())

                    }
                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            //  firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode)

          /*  val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        Toast.makeText(this@RegistrationActivity, "Success", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@RegistrationActivity, CourseSelectionActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Toast.makeText(this@RegistrationActivity, "Success", Toast.LENGTH_LONG).show()

                    }

                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
*/
        }
        }

    }

package com.trisys.rn.baseapp.activity.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.onBoarding.LoginActivity
import kotlinx.android.synthetic.main.fragment_otp.*

class OtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_otp)

        continueButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

}

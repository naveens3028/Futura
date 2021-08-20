package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.login.CourseSelectionActivity
import com.trisys.rn.baseapp.activity.login.OtpActivity
import kotlinx.android.synthetic.main.activity_register.*


class RegistrationActivity: AppCompatActivity() {

    private val myList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
            val intent = Intent(this, CourseSelectionActivity::class.java)
            startActivity(intent)
        }


    }
}
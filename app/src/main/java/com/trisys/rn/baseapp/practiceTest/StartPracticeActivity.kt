package com.trisys.rn.baseapp.practiceTest

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.model.QuestionNumberItem
import com.trisys.rn.baseapp.model.QuestionType
import com.trisys.rn.baseapp.practiceTest.adapter.QuestionNumberAdapter
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_start_practice.*
import kotlinx.android.synthetic.main.dialog_jump_to_questions.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class StartPracticeActivity : AppCompatActivity() {
    val questionNumberItem = ArrayList<QuestionNumberItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_practice)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = "Create Practice"

        saveForLater.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_jump_to_questions)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.attributes.gravity = Gravity.CENTER
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.close.setOnClickListener {
            dialog.cancel()
            dialog.hide()
        }
        questionNumberItem.add(QuestionNumberItem(1, QuestionType.ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(2, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(3, QuestionType.MARK_FOR_REVIEW))
        questionNumberItem.add(QuestionNumberItem(4, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(5, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(6, QuestionType.ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(7, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(8, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(9, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(10, QuestionType.ATTEMPT))
        val questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem)
        dialog.questionNumber.adapter = questionNumberAdapter
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_learn, menu)
            val item1 =
                menu.findItem(R.id.action_menu_notification).actionView.findViewById(R.id.layoutNotification) as RelativeLayout
            item1.setOnClickListener {
                startActivity(Intent(this, NotificationsActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
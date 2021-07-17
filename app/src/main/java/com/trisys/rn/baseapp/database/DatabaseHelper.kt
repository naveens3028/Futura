package com.trisys.rn.baseapp.database

import android.content.Context
import android.os.AsyncTask
import com.trisys.rn.baseapp.database.model.NotificationItem
import com.trisys.rn.baseapp.model.Quesion
import com.trisys.rn.baseapp.model.TestPaperVo
import com.trisys.rn.baseapp.model.TestResultsModel
import com.trisys.rn.baseapp.model.onBoarding.AverageBatchTests

class DatabaseHelper(context: Context) {
    private var db: AppDatabase? = null

    init {
        db = AppDatabase.getInstance(context)
    }

    fun saveAvg(notificationItem: AverageBatchTests) {

        db!!.averageBatchDao.addAvg(notificationItem)
    }

    fun getAllAverageBatchTest(): MutableList<AverageBatchTests> {
        return db!!.averageBatchDao.getAll()
    }

    fun saveResult(testResultsModel: TestResultsModel) {
        db!!.resultsDao.addResult(testResultsModel)
    }

    fun getAllResult(): MutableList<TestResultsModel> {
        return db!!.resultsDao.getAll()
    }

    fun getTestResponse(testPaperId: String): TestResultsModel {
        return db!!.resultsDao.getResponse(testPaperId)
    }

    fun saveNotificationItem(notificationItem: NotificationItem): Long {
        var notificationID: Long = 0


        object : AsyncTask<NotificationItem, Void, Long>() {
            override fun doInBackground(vararg params: NotificationItem): Long {
                if (params != null) {
                    val saveFavNews = params[0]
                    if (saveFavNews.notifyID > 0) {
                        db!!.notificationDao.update(notificationItem)
                    } else {
                        notificationID = db!!.notificationDao.add(notificationItem)
                    }
                } else {
                    notificationID = db!!.notificationDao.add(notificationItem)
                }
                return notificationID
            }


        }.execute(notificationItem)

        return notificationID
    }

    fun getAllNotification(): List<NotificationItem> {
        return db!!.notificationDao.getAll()
    }

    fun getAllUnreadNotification(): List<NotificationItem> {
        return db!!.notificationDao.getAllUnreadNotification()
    }

    fun saveTestPaper(testPaperVo: TestPaperVo) {
        db!!.testDAO.addTestPaper(testPaperVo)
    }

    fun addQuestions(question: Quesion) {
        db!!.testDAO.addQuestionList(question)
    }

    fun getQuestionList(testPaperId: String): MutableList<Quesion> {
        return db!!.testDAO.getQuestion(testPaperId)
    }

    fun updateAnswer(questionId: String, answer: String?) {
        db!!.testDAO.updateQuestion(questionId, answer)
    }

}
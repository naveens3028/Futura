package com.trisys.rn.baseapp.model

data class UpcomingLiveItem(
    var subject: String? = null,
    var imageID: Int = 0
)

data class CompletedLiveItem(
    var subject: String? = null,
    var date: String? = null,
    var lesson: String? = null,
    var color: Int = 0
)

data class SubTopicItem(
    var subject: String? = null,
)

data class StudyItem(
    var subject: String? = null,
    var lesson: String? = null,
    var count: String? = null,
    var imageID: Int = 0,
    var progress: Int = 0,
    var color: Int = 0,
)

data class ScheduledTestItem(
    var testName: String? = null,
    var date: String? = null,
    var mark: String? = null,
    var duration: String? = null,
    var color: Int = 0,
)
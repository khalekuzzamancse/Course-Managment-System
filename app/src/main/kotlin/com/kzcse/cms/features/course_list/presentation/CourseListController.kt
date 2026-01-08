package com.kzcse.cms.features.course_list.presentation

import com.kzcse.cms.core.ui.FeedbackController
import com.kzcse.cms.features.course_list.domain.CourseModel
import kotlinx.coroutines.flow.StateFlow

interface CourseListController: FeedbackController {
    val courses: StateFlow<List<CourseModel>>
    fun read()
    fun search(query: String)
}
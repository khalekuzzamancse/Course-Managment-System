package com.kzcse.cms.features.course_list.presentation

import com.kzcse.cms.features._core.logic.LoadingAndFeedbackController
import com.kzcse.cms.features.course_list.domain.CourseModel
import kotlinx.coroutines.flow.StateFlow

interface CourseListController: LoadingAndFeedbackController {
    val courses: StateFlow<List<CourseModel>>
    fun read()
    fun search(query: String)
}
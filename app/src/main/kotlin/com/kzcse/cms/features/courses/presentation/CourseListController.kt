package com.kzcse.cms.features.courses.presentation

import com.kzcse.cms.core.ui.FeedbackController
import com.kzcse.cms.features.courses.domain.CourseModel
import kotlinx.coroutines.flow.StateFlow

interface CourseListController: FeedbackController {
    val courses: StateFlow<List<CourseModel>>
    fun read()
    fun search(query: String)
}
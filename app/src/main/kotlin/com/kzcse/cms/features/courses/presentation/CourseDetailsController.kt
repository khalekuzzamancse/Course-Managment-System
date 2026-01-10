package com.kzcse.cms.features.courses.presentation

import com.kzcse.cms.core.ui.FeedbackController
import com.kzcse.cms.features.courses.domain.CourseModel
import kotlinx.coroutines.flow.StateFlow

interface CourseDetailsController: FeedbackController {
    val course: StateFlow<CourseModel?>
    fun read(id: String)
    fun enroll()

}
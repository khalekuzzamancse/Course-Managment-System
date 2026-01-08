package com.kzcse.cms.features.course_list.data

import com.kzcse.cms.core.ui.FeedbackController
import com.kzcse.cms.features.course_list.domain.CourseModel
import kotlinx.coroutines.flow.StateFlow

interface CourseDetailsController:FeedbackController {
    val course: StateFlow<CourseModel?>
    fun read(id: String)
    fun enroll()

}
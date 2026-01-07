package com.kzcse.cms.features.course_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kzcse.cms.features._core.logic.LoadingAndFeedbackController
import com.kzcse.cms.features._core.logic.LoadingAndFeedbackControllerImpl
import com.kzcse.cms.features.course_list.data.CourseRepositoryImpl
import com.kzcse.cms.features.course_list.domain.CourseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CourseListViewModel:CourseListController, LoadingAndFeedbackController by LoadingAndFeedbackControllerImpl(),
    ViewModel() {
    override val courses= MutableStateFlow(listOf<CourseModel>())
    private val repository= CourseRepositoryImpl()
    override fun read() {
      viewModelScope.launch {
          startLoading()
        val  models=repository.readOrThrow()
          courses.value=models
          delay(2000)
          stopLoading()
      }
    }

    override fun search(query: String) {
        TODO("Not yet implemented")
    }
}
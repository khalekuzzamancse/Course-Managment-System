package com.kzcse.cms.features.courses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kzcse.cms.core.language.Logger
import com.kzcse.cms.core.ui.FeedbackController
import com.kzcse.cms.core.ui.FeedbackControllerImpl
import com.kzcse.cms.features.courses.presentation.CourseDetailsController
import com.kzcse.cms.features.courses.data.CourseRepositoryImpl
import com.kzcse.cms.features.courses.domain.CourseModel
import com.kzcse.cms.features.courses.domain.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailsViewModel @Inject constructor(
    private val repository: CourseRepository
) : CourseDetailsController, FeedbackController by FeedbackControllerImpl(),
    ViewModel() {
      //  private val repository= CourseRepositoryImpl()
      override val course= MutableStateFlow<CourseModel?>(null)
    private var id=""
    override fun read(id: String) {
        this.id=id
        viewModelScope.launch {
            try {
                startLoading()
             val model= repository.detailsOrThrow(id)
                course.value=model
                Logger.on("CourseDetailsViewModel","read","model:$model")
                delay(2000)
            }
            catch (e: Throwable){
                updateFeedback(e)
            }
            finally {
                stopLoading()
            }
        }
    }

    override fun enroll() {
        viewModelScope.launch {
            try {
                startLoading()
              repository.enrollOrThrow(id)
                read(id)
            }
            catch (e: Throwable){
                updateFeedback(e)
                Logger.on("CourseDetailsViewModel","enroll","error:$e")
            }
            finally {
                stopLoading()
            }
        }
    }
}
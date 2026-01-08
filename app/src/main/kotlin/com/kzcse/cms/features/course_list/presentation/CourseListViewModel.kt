package com.kzcse.cms.features.course_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kzcse.cms.core.language.Logger
import com.kzcse.cms.core.ui.FeedbackController
import com.kzcse.cms.core.ui.FeedbackControllerImpl
import com.kzcse.cms.features.course_list.data.CourseRepositoryImpl
import com.kzcse.cms.features.course_list.domain.CourseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CourseListViewModel : CourseListController,
    FeedbackController by FeedbackControllerImpl(),
    ViewModel() {
    override val courses = MutableStateFlow(listOf<CourseModel>())
    private val repository = CourseRepositoryImpl()

    fun observe(flow: Flow<List<CourseModel>>) {
        viewModelScope.launch {
            flow.collect {
                courses.value = it
            }
        }
    }

    override fun read() {
        viewModelScope.launch {
            try {
                startLoading()
                val models = repository.readOrThrow()
                observe(models)
                delay(1000)
            }
            catch (e: Throwable){
                updateFeedback(e)
            }
            finally {
                stopLoading()
            }


        }
    }

    override fun search(query: String) {
        viewModelScope.launch {
            try {
                startLoading()
                val models = repository.searchOrThrow(query)
                courses.value = models
                Logger.on("CourseListViewModel","search","${models.size}")
                delay(1000)
            }
            catch (e: Throwable){
                Logger.on("CourseListViewModel","search:Error","$e")
                updateFeedback(e)
            }
            finally {
                stopLoading()

            }
        }

    }
}
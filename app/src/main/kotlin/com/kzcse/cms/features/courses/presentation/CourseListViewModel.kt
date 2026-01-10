package com.kzcse.cms.features.courses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kzcse.cms.core.language.Logger
import com.kzcse.cms.core.ui.FeedbackController
import com.kzcse.cms.core.ui.FeedbackControllerImpl
import com.kzcse.cms.features._core.MonitorConnectivity
import com.kzcse.cms.features.courses.domain.CourseModel
import com.kzcse.cms.features.courses.domain.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repository: CourseRepository
) : CourseListController,
    FeedbackController by FeedbackControllerImpl(),
    ViewModel() {
    override val courses = MutableStateFlow(listOf<CourseModel>())
    //private val repository = CourseRepositoryImpl()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            MonitorConnectivity.isConnected.collect { available->
                if (available){
                    Logger.on("CourseListViewModel","NetworkMonitor","available=$available")
                   read()
                }

            }

        }
    }

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
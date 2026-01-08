package com.kzcse.cms.core.ui

import com.kzcse.cms.core.language.CustomException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
interface FeedbackController{
    val isLoading: StateFlow<Boolean>
    val messageToUi: StateFlow<String?>
    fun startLoading()
    fun stopLoading()
    fun updateFeedback(message: String)
    fun updateFeedback(exception: Throwable)
    companion object Companion {
        fun create():FeedbackController=FeedbackControllerImpl()
    }
}
class FeedbackControllerImpl:FeedbackController{
    private val scope= CoroutineScope(Dispatchers.Default)
    private val _isLoading= MutableStateFlow(false)
    private val _feedbackMessage=MutableStateFlow<String?>(null)
    override val isLoading=_isLoading.asStateFlow()
    override val messageToUi=_feedbackMessage.asStateFlow()
    override  fun startLoading()=_isLoading.update { true }
    override fun stopLoading()=_isLoading.update { false }
    override fun updateFeedback(message:String){
        scope.launch {
            _feedbackMessage.update { message }
            delay(5_000)
            _feedbackMessage.update { null }
        }
    }

    override fun updateFeedback(exception: Throwable) {
        if(exception is CustomException)
            updateFeedback(exception.message)
        else updateFeedback("Something is went wrong")
    }
}
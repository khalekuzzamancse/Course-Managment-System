package com.kzcse.cms

import com.kzcse.cms.core.data_src.api.CourseRemoteApiFactory
import com.kzcse.cms.core.language.Logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ApiTest {
    @Test
    fun  readTest(){
        runBlocking {
            val api= CourseRemoteApiFactory.createApi()
            val result=api.readOrThrow()
            Logger.on("ApiTest","readTest","result",result.toString())
        }
    }
}
package com.kzcse.cms.core.data_src.api

import com.kzcse.cms.core.data_src.remote.CourseRDSTemplate
import com.kzcse.cms.core.data_src.remote.CourseRDSTemplateImpl

/// The abstract factory
interface ApiFactory {
    fun createApi(): CourseApi
}
object CourseRemoteApiFactory : ApiFactory {
    override fun createApi(): CourseApi {
        return CourseRDSTemplateImpl()
    }
}
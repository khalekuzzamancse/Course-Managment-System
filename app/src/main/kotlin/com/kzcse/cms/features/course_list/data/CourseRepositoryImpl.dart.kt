package com.kzcse.cms.features.course_list.data

import com.kzcse.cms.core.data_src.api.CourseEntity
import com.kzcse.cms.core.data_src.api.CourseRemoteApiFactory
import com.kzcse.cms.core.data_src.api.InstructorEntity
import com.kzcse.cms.features.course_list.domain.CourseModel
import com.kzcse.cms.features.course_list.domain.CourseRepository
import com.kzcse.cms.features.course_list.domain.InstructorModel
import kotlin.collections.map

class CourseRepositoryImpl : CourseRepository {
    private val api = CourseRemoteApiFactory.createApi()
    override suspend fun readOrThrow(): List<CourseModel> {
        return api.readOrThrow().toModelList()
    }

    override suspend fun searchOrThrow(): List<CourseModel> {
        return api.searchOrThrow().toModelList()
    }

    override suspend fun detailsOrThrow(id: String): CourseModel {
        return api.detailsOrThrow(id.toInt()).toModel()
    }

    fun List<CourseEntity>.toModelList(): List<CourseModel> =
        map { it.toModel() }

    fun CourseEntity.toModel(): CourseModel =
        CourseModel(
            id = id,
            name = name,
            descriptionShort = descriptionShort,
            instructor = instructor.toModel(),
            durationWeeks = durationWeeks,
            priceUsd = priceUsd,
            isPremium = isPremium,
            tags = tags,
            rating = rating
        )

    fun InstructorEntity.toModel(): InstructorModel =
        InstructorModel(
            name = name,
            expertiseLevel = expertiseLevel
        )

}
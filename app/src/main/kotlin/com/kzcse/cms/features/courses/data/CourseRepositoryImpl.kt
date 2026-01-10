package com.kzcse.cms.features.courses.data

import com.kzcse.cms.core.data_src.api.CourseEntity
import com.kzcse.cms.core.data_src.api.CourseRemoteApiFactory
import com.kzcse.cms.core.data_src.api.InstructorEntity
import com.kzcse.cms.core.data_src.local.CourseSchema
import com.kzcse.cms.core.data_src.local.InstructorSchema
import com.kzcse.cms.core.data_src.local.RoomDbProvider
import com.kzcse.cms.core.language.CustomException
import com.kzcse.cms.core.language.Logger
import com.kzcse.cms.features.courses.domain.CourseModel
import com.kzcse.cms.features.courses.domain.CourseRepository
import com.kzcse.cms.features.courses.domain.InstructorModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor() : CourseRepository {
    private val api = CourseRemoteApiFactory.createApi()
    private val localDb = RoomDbProvider.dbOrThrow().courseDao()
    override suspend fun readOrThrow(): Flow<List<CourseModel>> {
        try {
            val entities= api.readOrThrow().toModelList()
           localDb.insertCourses(entities.map {
               val enrolled = localDb.getCourseById(it.id)?.enrolled
               it.toSchema(enrolled?:false)
           })
           return localDb.getAllCourses().map { it.map { schema -> schema.toModel() } }
        }
        catch (e: Throwable){
            Logger.on("CourseRepositoryImpl","readOrThrow","error:$e")
           return localDb.getAllCourses().map { it.map { schema -> schema.toModel() } }
        }

    }

    override suspend fun searchOrThrow(query: String): List<CourseModel> {
        return localDb.searchCourses(query).map{schema -> schema.toModel() }
    }

    override suspend fun detailsOrThrow(id: String): CourseModel {
        try {
            return localDb.getCourseById(id)!!.toModel()
        }
        catch (_: Throwable){
            throw Exception("Course not found")
        }

    }

    override suspend fun enrollOrThrow(id: String) {
        try {
             localDb.enrollCourse(id)
        }
        catch (_: Throwable){
            throw Exception("Unable to enroll")
        }
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
            rating = rating,
            isEnrolled = false
        )

    fun InstructorEntity.toModel(): InstructorModel =
        InstructorModel(
            name = name,
            expertiseLevel = expertiseLevel
        )

}

// Mapper for Instructor
fun InstructorModel.toSchema(): InstructorSchema {
    return InstructorSchema(
        name = this.name,
        expertiseLevel = this.expertiseLevel
    )
}

// Mapper for Course
fun CourseModel.toSchema(enrolled: Boolean): CourseSchema {
    return CourseSchema(
        id = this.id,
        name = this.name,
        descriptionShort = this.descriptionShort,
        instructor = this.instructor.toSchema(),
        durationWeeks = this.durationWeeks,
        priceUsd = this.priceUsd,
        isPremium = this.isPremium,
        tags = this.tags.joinToString(","),
        rating = this.rating,
        enrolled = enrolled
    )
}

// Mapper for InstructorSchema -> InstructorModel
fun InstructorSchema.toModel(): InstructorModel {
    return InstructorModel(
        name = this.name,
        expertiseLevel = this.expertiseLevel
    )
}

// Mapper for CourseSchema -> CourseModel
fun CourseSchema.toModel(): CourseModel {
    return CourseModel(
        id = this.id,
        name = this.name,
        descriptionShort = this.descriptionShort,
        instructor = this.instructor.toModel(),
        durationWeeks = this.durationWeeks,
        priceUsd = this.priceUsd,
        isPremium = this.isPremium,
        tags = this.tags.split(","),
        rating = this.rating,
        isEnrolled = this.enrolled
    )
}


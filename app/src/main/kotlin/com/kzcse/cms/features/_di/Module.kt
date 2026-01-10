package com.kzcse.cms.features._di

import com.kzcse.cms.features.courses.data.CourseRepositoryImpl
import com.kzcse.cms.features.courses.domain.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class Module {

    @Binds
    abstract fun bindCourseRepository(
        impl: CourseRepositoryImpl
    ): CourseRepository

}

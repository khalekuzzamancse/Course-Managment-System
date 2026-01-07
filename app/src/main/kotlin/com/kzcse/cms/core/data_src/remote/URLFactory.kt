@file:Suppress("ClassName")
package com.kzcse.cms.core.data_src.remote
//The abstract factory
interface URLFactory {
    companion object{
        val urls=_MockUrlFactory()
    }
    val readCourse: String
    fun readCourseDetails(id: String): String
}
class _MockUrlFactory: URLFactory {
    private val base="  https://programmingherotask.free.beeceptor.com"
    override val readCourse="${base}/courses"
    override fun readCourseDetails(id: String): String {
        TODO("Not yet implemented")
    }


}
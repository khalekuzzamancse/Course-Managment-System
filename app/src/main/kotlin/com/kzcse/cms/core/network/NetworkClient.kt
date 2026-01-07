package com.kzcse.cms.core.network


interface NetworkClient {
    suspend fun getOrThrow(url: String, headers: Headers? = null): String
    suspend fun postOrThrow(url: String, payload: Any, headers: Headers? = null): String
    suspend fun putOrThrow(url: String, payload: Any? = null, headers: Headers? = null): String
    suspend fun patchOrThrow(url: String, payload: Any, headers: Headers? = null): String
    suspend fun deleteOrThrow(url: String, headers: Headers? = null): String
    companion object{
        fun  createBase():NetworkClient=KtorNetworkClient()
        fun createDecorator():NetworkClient= NetworkClientDecorator.create(createBase())
    }
}

data class HttpHeader(val key: String, val value: String) {
    override fun toString(): String = "$key: $value"
}

class Headers(val headers: List<HttpHeader>) {

    fun toMap(): Map<String, String> {
        return headers.associate { it.key to it.value }
    }

    override fun toString(): String = headers.toString()

    companion object {
        fun createJWTAuthHeader(token: String): Headers {
            return Headers(listOf(HttpHeader("Authorization", "Bearer $token")))
        }

        fun createAuthorizationHeader(token: String): Headers {
            return Headers(listOf(HttpHeader("Authorization", token)))
        }
    }
}
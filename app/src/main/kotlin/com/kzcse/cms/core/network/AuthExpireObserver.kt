package com.kzcse.cms.core.network

import com.kzcse.cms.core.language.CustomException
import com.kzcse.cms.core.language.UnauthorizedException

interface AuthExpireObserver {
    fun onSessionExpire()
}

interface TokenManager {
    suspend fun updateAccessToken()
    suspend fun getTokenOrThrow(): String
}

class NetworkClientDecorator private constructor(
    private val client: NetworkClient
) : NetworkClient {

    companion object {
        private var observer: AuthExpireObserver? = null
        private var tokenManager: TokenManager? = null

        fun setTokenManager(manager: TokenManager) {
            tokenManager = manager
        }

        fun registerAsListener(observer: AuthExpireObserver) {
            this.observer = observer
        }

        fun unRegister() {
            observer = null
        }

        fun create(client: NetworkClient): NetworkClient {
            return NetworkClientDecorator(client)
        }
    }

    private val tag = this::class.simpleName ?: "NetworkClientDecorator"

    override suspend fun getOrThrow(url: String, headers: Headers?): String {
        return withAuthStrategy { h -> client.getOrThrow(url, h) }
    }

    override suspend fun postOrThrow(url: String, payload: Any, headers: Headers?): String {
        return withAuthStrategy { h -> client.postOrThrow(url, payload, h) }
    }

    override suspend fun putOrThrow(url: String, payload: Any?, headers: Headers?): String {
        return withAuthStrategy { h -> client.putOrThrow(url, payload, h) }
    }

    override suspend fun patchOrThrow(url: String, payload: Any, headers: Headers?): String {
        return withAuthStrategy { h -> client.patchOrThrow(url, payload, h) }
    }

    override suspend fun deleteOrThrow(url: String, headers: Headers?): String {
        return withAuthStrategy { h -> client.deleteOrThrow(url, h) }
    }

    private suspend fun withAuthStrategy(action: suspend (Headers) -> String): String {
        return try {
            withRetry(action)
        } catch (e: Throwable) {
            if (e is UnauthorizedException) {
                observer?.onSessionExpire()
            }
            throw e
        }
    }

    private suspend fun withRetry(action: suspend (Headers) -> String): String {
        try {
            val header = createHeader()
            return action(header)
        } catch (e: Throwable) {
            if (e is UnauthorizedException) {
                val manager = tokenManager
                    ?: throw CustomException("Token Manager not provided", tag)
                manager.updateAccessToken()
                val newHeader = createHeader()
                return action(newHeader)
            } else {
                throw e
            }
        }
    }

    private suspend fun createHeader(): Headers {
        val manager = tokenManager
            ?: throw CustomException("Token Manager not provided", tag)
        val token = manager.getTokenOrThrow()
        return Headers.createAuthorizationHeader("Token $token")
    }
}


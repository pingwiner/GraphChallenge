package com.cats32.challenge.network


sealed class Response<out T> {
    data class Success<out T>(val success: T) : Response<T>()
    data class Error(
        val exception: Exception, val code: Int? = null,
        val response: ErrorResponse? = null
    ) : Response<Nothing>() {
        constructor(message: String) : this(Exception(message))
        constructor(
            message: String, errorCode: Int,
            errorResponse: ErrorResponse? = null
        ) : this(
            Exception(message),
            errorCode, errorResponse
        )
    }

    val isSuccess: Boolean get() = this is Success

    val body: T
        get() = when (this) {
            is Success -> success
            is Error -> throw Throwable()
        }

    val bodyOrNull: T?
        get() = when (this) {
            is Success -> success
            is Error -> null
        }

    val throwable: Throwable?
        get() = when (this) {
            is Success -> null
            is Error -> exception
        }

    val error: String?
        get() = when (this) {
            is Success -> null
            is Error -> exception.message
        }

    val errorResponse: ErrorResponse?
        get() = when (this) {
            is Success -> null
            is Error -> response
        }

    val errorOrBody: String?
        get() = when (this) {
            is Success -> "$body"
            is Error -> exception.message
        }

    val errorCode: Int?
        get() = when (this) {
            is Success -> null
            is Error -> code
        }

    val errorText: String?
        get() = when (this) {
            is Success -> null
            is Error -> {
                errorResponse?.type ?: error
            }
        }
}
package com.cats32.challenge.network

import com.google.gson.Gson
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import kotlin.math.min


suspend fun <T : Any> safeCall(timeout: Long = 0, parseError: Boolean = false, call: suspend () -> T): Response<T> {
    return try {
        val data = if (timeout > 0) {
            withTimeout(timeout) {
                call.invoke()
            }
        } else {
            call.invoke()
        }
        Response.Success(data)
    } catch (e: Exception) {
        when (e) {
            is HttpException -> {
                val errorBody = getErrorBody(e)
                val code = e.code()
                val message = getMessage(code, errorBody)
                Response.Error(message, code, getErrorResponse(errorBody))
            }
            else -> {
                Response.Error(e)
            }
        }
    }
}

private fun getErrorBody(e: HttpException) = e.response()?.errorBody()?.string()?.trim() ?: ""

private fun getMessage(code: Int, body: String): String {
    val headSize = min(body.length, 150)
    val tailSize = min(body.length - headSize, 150)
    val head = body.substring(0, headSize)
    val shortBody = if (tailSize > 0) {
        val tail = body.substring(body.length - tailSize)
        "$head\n...\n$tail"
    } else {
        head
    }
    return "HTTP $code:\n$shortBody"
}

private fun getErrorResponse(errorBody: String): ErrorResponse? {
    return try {
        Gson().fromJson(errorBody, ErrorResponse::class.java)
    } catch (e: Throwable) {
        null
    }
}
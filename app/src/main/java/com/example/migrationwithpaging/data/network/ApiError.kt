package com.example.migrationwithpaging.data.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody


/**
 * @Details
 * @Author Ranosys Technologies
 * @Date 31-Jul-2019
 */

open class ApiErrorParser {
    fun parseApiError(errorBody: ResponseBody?): ApiError =
        Gson().fromJson(errorBody.toString(), ApiError::class.java)
}


data class ApiError(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String
)
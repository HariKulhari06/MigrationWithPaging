package com.example.migrationwithpaging.data.network

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * @Details class for Repository
 * @Author Ranosys Technologies
 * @Date 03-Apr-2019
 */

object ApiRepository {

    fun <R> callApi(
        request: Deferred<Response<R>>,
        apiCallBack: ApiCallback<R>?
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    apiCallBack?.onSuccess(response.body())
                } else {
                    response.errorBody()?.let { apiCallBack?.onError(ApiErrorParser().parseApiError(response.errorBody())) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                apiCallBack?.onException(e)
            }
        }
    }

}

interface ApiCallback<T> {
    fun onException(error: Throwable)
    fun onError(errorModel: ApiError)
    fun onSuccess(t: T?)
}
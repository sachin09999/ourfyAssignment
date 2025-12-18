package com.sachin.assignmnet.data.remote

import com.sachin.assignmnet.data.local.HistoryEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("upload")
    suspend fun uploadHistory(@Body history: List<HistoryEntity>): Response<Unit>
}

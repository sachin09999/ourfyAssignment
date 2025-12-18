package com.sachin.assignmnet

import android.app.Application
import com.sachin.assignmnet.data.local.AppDatabase
import com.sachin.assignmnet.data.remote.ApiService
import com.sachin.assignmnet.data.repository.HistoryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OurfyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    
    val apiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://sachin-ourfy-test.free.beeceptor.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val repository by lazy { HistoryRepository(database.historyDao(), apiService) }
}


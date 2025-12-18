package com.sachin.assignmnet.data.repository

import com.sachin.assignmnet.data.local.HistoryDao
import com.sachin.assignmnet.data.local.HistoryEntity
import com.sachin.assignmnet.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class HistoryRepository(
    private val historyDao: HistoryDao,
    private val apiService: ApiService
) {
    val allHistory: Flow<List<HistoryEntity>> = historyDao.getAllHistory()

    suspend fun insert(url: String) {
        val history = HistoryEntity(url = url, timestamp = System.currentTimeMillis())
        historyDao.insert(history)
    }

    suspend fun deleteHistory() {
        historyDao.deleteAll()
    }

    suspend fun getSnapshot(): List<HistoryEntity> {
        return historyDao.getAllHistoryList()
    }

    suspend fun uploadHistory(): Boolean {
        return try {
            val history = getSnapshot()
            val response = apiService.uploadHistory(history)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}


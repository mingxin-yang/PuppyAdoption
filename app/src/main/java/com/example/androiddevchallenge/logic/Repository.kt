package com.example.androiddevchallenge.logic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val dataHelper: DataHelper) {
    suspend fun getDogs() = withContext(Dispatchers.IO){
        dataHelper.getDogs()
    }
}
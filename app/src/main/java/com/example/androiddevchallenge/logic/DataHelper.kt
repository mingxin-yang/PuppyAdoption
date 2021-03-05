package com.example.androiddevchallenge.logic

import com.example.androiddevchallenge.model.Dog

class DataHelper{
    suspend fun getDogs(): List<Dog>
}
package com.example.androiddevchallenge.logic

import com.example.androiddevchallenge.GlobalApp
import com.example.androiddevchallenge.model.Dog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList
import kotlin.reflect.typeOf

class DataHelper{
    suspend fun getDogs(): List<Dog> = coroutineScope {
        var dogs: List<Dog> = ArrayList();
        val job = launch(Dispatchers.IO) {
            try {
                val assertManager = GlobalApp.context.assets
                val inputReader = InputStreamReader(assertManager.open("dogs.json"))
                val jsonString = BufferedReader(inputReader).readText()
                val typeOf = object : TypeToken<List<Dog>>() {}.type
                dogs = Gson().fromJson(jsonString,typeOf)
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }

        job.join()
        dogs

    }
}
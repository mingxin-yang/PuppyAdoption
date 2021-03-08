package com.example.androiddevchallenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.logic.Repository
import com.example.androiddevchallenge.model.Dog
import com.example.androiddevchallenge.util.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val dogsLiveData: LiveData<Resource<List<Dog>>>
        get() = _dogsLiveData
    private val _dogsLiveData = MutableLiveData<Resource<List<Dog>>>()

    private val dogs = mutableListOf<Dog>()

    private val handler = CoroutineExceptionHandler{_,throwable->
        throwable.printStackTrace()
        _dogsLiveData.value = Resource.error(throwable.message?:"Uncaught exception happens")

    }

    fun readAndParseData() = viewModelScope.launch(handler) {
        _dogsLiveData.value = Resource.loading()
        dogs.addAll(repository.getDogs())
        _dogsLiveData.value = Resource.success(dogs)
        Log.e("ymx", "readAndParseData: "+dogs.toString() )
    }

    fun setDogAdopted(position: Int){
        dogs[position].adopted=true
        _dogsLiveData.value = _dogsLiveData.value
    }



}
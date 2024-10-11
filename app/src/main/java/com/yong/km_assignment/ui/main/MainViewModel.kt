package com.yong.km_assignment.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.repository.DefaultRouteListRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val repository = DefaultRouteListRepository()
    private val _routeList: MutableLiveData<RouteList> = MutableLiveData()
    val routeList: LiveData<RouteList> = _routeList

    fun getRouteList() {
        viewModelScope.launch {
            repository.getRouteList().let {
                _routeList.postValue(it)
            }
        }
    }

    fun onItemClick(position: Int) {
        Log.d("RouteList", "${routeList.value?.routeList?.get(position)}")
    }
}
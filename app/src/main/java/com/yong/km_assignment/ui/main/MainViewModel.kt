package com.yong.km_assignment.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.model.RouteListItem
import com.yong.km_assignment.data.repository.RouteListRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val repository = RouteListRepository()
    private val _routeList: MutableLiveData<RouteList> = MutableLiveData()
    val routeList: LiveData<RouteList> = _routeList

    fun getRouteList() {
        viewModelScope.launch {
            repository.getRouteList().let {
                _routeList.postValue(it)
            }
        }
    }

    fun onItemClick(route: RouteListItem) {
        Log.d("RouteList", "${route.routeFrom} -> ${route.routeTo}")
    }
}
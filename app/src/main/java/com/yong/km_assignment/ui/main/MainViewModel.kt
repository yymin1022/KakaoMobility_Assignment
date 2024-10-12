package com.yong.km_assignment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.repository.RouteListRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _repositoryList = RouteListRepository()
    private val _routeList: MutableLiveData<RouteList> = MutableLiveData()
    val routeList: LiveData<RouteList> = _routeList

    fun getRouteList() {
        viewModelScope.launch {
            _repositoryList.getRouteList().let {
                if(it != null) {
                    _routeList.postValue(it)
                }
            }
        }
    }
}
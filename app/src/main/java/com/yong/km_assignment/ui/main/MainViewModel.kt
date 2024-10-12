package com.yong.km_assignment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.model.RouteListItem
import com.yong.km_assignment.data.repository.RouteDetailRepository
import com.yong.km_assignment.data.repository.RouteListRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _repositoryDetail = RouteDetailRepository()
    private val _repositoryList = RouteListRepository()
    private val _routeDetail: MutableLiveData<List<RouteDetail>?> = MutableLiveData()
    private val _routeList: MutableLiveData<RouteList> = MutableLiveData()
    val routeDetail: LiveData<List<RouteDetail>?> = _routeDetail
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

    fun getRouteDetail(route: RouteListItem) {
        _routeDetail.postValue(null)
        viewModelScope.launch {
            _repositoryDetail.getRouteDetail(route.routeFrom, route.routeTo).let {
                _routeDetail.postValue(it)
            }
        }
    }
}
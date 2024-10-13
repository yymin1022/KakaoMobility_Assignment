package com.yong.km_assignment.ui.mapview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.data.repository.RouteDetailRepository
import com.yong.km_assignment.data.repository.RouteInfoRepository
import kotlinx.coroutines.launch

class MapviewViewModel: ViewModel() {
    private val _repositoryDetail = RouteDetailRepository()
    private val _repositoryInfo = RouteInfoRepository()
    private val _routeDetail: MutableLiveData<List<RouteDetail>?> = MutableLiveData()
    private val _routeDetailLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _routeInfo: MutableLiveData<RouteInfo?> = MutableLiveData()
    val routeDetail: LiveData<List<RouteDetail>?> = _routeDetail
    val routeDetailLoaded: LiveData<Boolean> = _routeDetailLoaded
    val routeInfo: LiveData<RouteInfo?> = _routeInfo

    fun getRouteDetail(
        routeFrom: String,
        routeTo: String
    ) {
        viewModelScope.launch {
            _repositoryDetail.getRouteDetail(routeFrom, routeTo).let {
                _routeDetail.postValue(it)
                _routeDetailLoaded.postValue(true)
            }
        }
    }

    fun getRouteInfo(
        routeFrom: String,
        routeTo: String
    ) {
        viewModelScope.launch {
            _repositoryInfo.getRouteInfo(routeFrom, routeTo).let {
                _routeInfo.postValue(it)
            }
        }
    }
}
package com.yong.km_assignment.ui.mapview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.repository.RouteDetailRepository
import kotlinx.coroutines.launch

class MapviewViewModel: ViewModel() {
    private val _repositoryDetail = RouteDetailRepository()
    private val _routeDetail: MutableLiveData<List<RouteDetail>?> = MutableLiveData()
    val routeDetail: LiveData<List<RouteDetail>?> = _routeDetail

    fun getRouteDetail(routeFrom: String, routeTo: String) {
        viewModelScope.launch {
            _repositoryDetail.getRouteDetail(routeFrom, routeTo).let {
                _routeDetail.postValue(it)
            }
        }
    }

    fun setRouteDetail(routeDetail: List<RouteDetail>) {
        _routeDetail.postValue(routeDetail)
    }
}
package com.yong.km_assignment.ui.mapview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yong.km_assignment.data.model.RouteDetail

class MapviewViewModel: ViewModel() {
    private val _routeDetail: MutableLiveData<List<RouteDetail>> = MutableLiveData()
    val routeDetail: LiveData<List<RouteDetail>> = _routeDetail

    fun setRouteDetail(routeDetail: List<RouteDetail>) {
        _routeDetail.postValue(routeDetail)
    }
}
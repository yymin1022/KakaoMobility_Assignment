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
    private val _routeDetailLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _routeInfo: MutableLiveData<RouteInfo?> = MutableLiveData()
    var routeDetail: List<RouteDetail>? = listOf()
    val routeDetailLoaded: LiveData<Boolean> = _routeDetailLoaded
    val routeInfo: LiveData<RouteInfo?> = _routeInfo
    var errCode: Int = 0
    var errMessage = "Success"

    fun getRouteDetail(
        routeFrom: String,
        routeTo: String
    ) {
        viewModelScope.launch {
            _repositoryDetail.getRouteDetail(routeFrom, routeTo).let {
                routeDetail = it.body()
                if(it.body() == null) {
                    errCode = it.code()
                    errMessage = when(errCode) {
                        404 -> "Not Found"
                        500 -> "Server Error"
                        else -> "Unknown Error"
                    }
                }
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
                _routeInfo.postValue(it.body())
            }
        }
    }
}
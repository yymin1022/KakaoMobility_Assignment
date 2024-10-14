package com.yong.km_assignment.ui.mapview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.data.repository.RouteDetailRepository
import com.yong.km_assignment.data.repository.RouteInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapviewViewModel: ViewModel() {
    private val _repositoryDetail = RouteDetailRepository()
    private val _repositoryInfo = RouteInfoRepository()
    private val _routeDetailLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _routeInfo: MutableStateFlow<RouteInfo?> = MutableStateFlow(null)
    var routeDetail: List<RouteDetail>? = listOf()
    val routeDetailLoaded: StateFlow<Boolean> = _routeDetailLoaded
    val routeInfo: StateFlow<RouteInfo?> = _routeInfo
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
                _routeDetailLoaded.value = true
            }
        }
    }

    fun getRouteInfo(
        routeFrom: String,
        routeTo: String
    ) {
        viewModelScope.launch {
            _repositoryInfo.getRouteInfo(routeFrom, routeTo).let {
                _routeInfo.value = it.body()
            }
        }
    }
}
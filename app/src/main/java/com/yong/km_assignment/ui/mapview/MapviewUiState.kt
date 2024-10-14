package com.yong.km_assignment.ui.mapview

import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteInfo

sealed class UIState {
    data object Loading: UIState()
    data class Success(val routeDetail: List<RouteDetail>, val routeInfo: RouteInfo?): UIState()
    data class Error(val errorCode: Int, val errorMessage: String): UIState()
}

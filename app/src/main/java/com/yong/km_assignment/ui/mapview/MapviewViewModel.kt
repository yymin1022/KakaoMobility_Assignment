package com.yong.km_assignment.ui.mapview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.repository.RouteDetailRepository
import com.yong.km_assignment.data.repository.RouteInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapviewViewModel: ViewModel() {
    private val _repositoryDetail = RouteDetailRepository()
    private val _repositoryInfo = RouteInfoRepository()

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    fun getRouteDetailAndInfo(routeFrom: String, routeTo: String) {
        viewModelScope.launch {
            try {
                val routeDetailResponse = _repositoryDetail.getRouteDetail(routeFrom, routeTo)
                val routeInfoResponse = _repositoryInfo.getRouteInfo(routeFrom, routeTo)

                val routeDetail = routeDetailResponse.body()
                val routeInfo = routeInfoResponse.body()

                if (routeDetail == null || routeInfo == null) {
                    val errorCode = routeDetailResponse.code().takeIf { routeDetail == null } ?: routeInfoResponse.code()
                    val errorMessage = when (errorCode) {
                        404 -> "Not Found"
                        500 -> "Server Error"
                        else -> "Unknown Error"
                    }
                    _uiState.value = UIState.Error(errorCode, errorMessage)
                } else {
                    _uiState.value = UIState.Success(routeDetail, routeInfo)
                }
            } catch (e: Exception) {
                _uiState.value = UIState.Error(-1, e.message ?: "Unknown Error")
            }
        }
    }
}
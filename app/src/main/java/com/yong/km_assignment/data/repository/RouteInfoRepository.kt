package com.yong.km_assignment.data.repository

import com.yong.km_assignment.data.api.RouteApi
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.util.ApiUtil
import retrofit2.Response

class RouteInfoRepository {
    private val _api: RouteApi = ApiUtil.getRouteApi()
    suspend fun getRouteInfo(routeFrom: String, routeTo: String): Response<RouteInfo?> {
        _api.getRouteInfo(routeFrom, routeTo).let { apiResult ->
            return apiResult
        }
    }
}
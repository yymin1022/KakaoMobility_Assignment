package com.yong.km_assignment.data.repository

import com.yong.km_assignment.data.api.RouteApi
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.util.ApiUtil

class RouteInfoRepository {
    private val api: RouteApi = ApiUtil.getRouteApi()
    suspend fun getRouteInfo(routeFrom: String, routeTo: String): RouteInfo? {
        api.getRouteInfo(routeFrom, routeTo).let { apiResult ->
            return apiResult.body()
        }
    }
}
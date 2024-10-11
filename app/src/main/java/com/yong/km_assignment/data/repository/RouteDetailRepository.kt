package com.yong.km_assignment.data.repository

import com.yong.km_assignment.data.api.RouteApi
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.util.ApiUtil

class RouteDetailRepository {
    private val api: RouteApi = ApiUtil.getRouteApi()
    suspend fun getRouteDetail(routeFrom: String, routeTo: String): List<RouteDetail> {
        api.getRouteDetail(routeFrom, routeTo).let {
            return it.body() as List<RouteDetail>
        }
    }
}
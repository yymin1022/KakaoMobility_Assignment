package com.yong.km_assignment.data.repository

import com.yong.km_assignment.data.api.RouteApi
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.util.ApiUtil
import retrofit2.Response

class RouteDetailRepository {
    private val _api: RouteApi = ApiUtil.getRouteApi()
    suspend fun getRouteDetail(routeFrom: String, routeTo: String): Response<List<RouteDetail>?> {
        _api.getRouteDetail(routeFrom, routeTo).let { apiResult ->
            return apiResult
        }
    }
}
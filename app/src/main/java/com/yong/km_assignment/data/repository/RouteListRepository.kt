package com.yong.km_assignment.data.repository

import com.yong.km_assignment.data.api.RouteApi
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.util.ApiUtil
import retrofit2.Response

class RouteListRepository {
    private val api: RouteApi = ApiUtil.getRouteApi()
    suspend fun getRouteList(): Response<RouteList?> {
        api.getRouteList().let { apiResult ->
            return apiResult
        }
    }
}
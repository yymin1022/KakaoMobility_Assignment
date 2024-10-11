package com.yong.km_assignment.data.repository

import com.yong.km_assignment.data.model.ApiResult
import com.yong.km_assignment.data.model.RouteList

interface RouteListRepository {
    fun getRouteList(): RouteList
}

class DefaultRouteListRepository: RouteListRepository {
    override fun getRouteList(): RouteList {
        return RouteList(ApiResult.API_OK, listOf())
    }
}
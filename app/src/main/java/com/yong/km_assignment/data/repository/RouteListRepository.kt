package com.yong.km_assignment.data.repository

import android.util.Log
import com.yong.km_assignment.data.api.RouteApi
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.util.ApiUtil

interface RouteListRepository {
    suspend fun getRouteList(): RouteList
}

class DefaultRouteListRepository: RouteListRepository {
    private val api: RouteApi = ApiUtil.getRouteApi()
    override suspend fun getRouteList(): RouteList {
        api.getRouteList().let {
            Log.d("RouteList Repository", it.body().toString())
            return it.body() as RouteList
        }
    }
}
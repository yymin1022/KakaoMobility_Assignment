package com.yong.km_assignment.data.repository

import android.util.Log
import com.yong.km_assignment.data.api.RouteApi
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.util.ApiUtil

class RouteListRepository {
    private val api: RouteApi = ApiUtil.getRouteApi()
    suspend fun getRouteList(): RouteList {
        api.getRouteList().let {
            Log.d("RouteList Repository", it.body().toString())
            return it.body() as RouteList
        }
    }
}
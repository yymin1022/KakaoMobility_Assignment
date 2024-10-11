package com.yong.km_assignment.data.api

import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.data.model.RouteList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RouteApi {
    @GET("")
    suspend fun getRouteDetail(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<RouteDetail>

    @GET("")
    suspend fun getRouteInfo(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<RouteInfo>

    @GET("")
    suspend fun getRouteList(): Response<RouteList>
}
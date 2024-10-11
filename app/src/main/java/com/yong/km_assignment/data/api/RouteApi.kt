package com.yong.km_assignment.data.api

import com.yong.km_assignment.data.model.ApiRouteDetail
import com.yong.km_assignment.data.model.ApiRouteInfo
import com.yong.km_assignment.data.model.ApiRouteList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RouteApi {
    @Headers("Content-Type: application/json")
    @GET("/api/v1/coding-assignment/routes")
    suspend fun getRouteDetail(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<List<ApiRouteDetail>>

    @Headers("Content-Type: application/json")
    @GET("/api/v1/coding-assignment/distance-time")
    suspend fun getRouteInfo(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<ApiRouteInfo>

    @GET("/api/v1/coding-assignment/locations")
    suspend fun getRouteList(): Response<ApiRouteList>
}
package com.yong.km_assignment.data.api

import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.data.model.RouteList
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
    ): Response<List<RouteDetail>?>

    @Headers("Content-Type: application/json")
    @GET("/api/v1/coding-assignment/distance-time")
    suspend fun getRouteInfo(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<RouteInfo?>

    @GET("/api/v1/coding-assignment/locations")
    suspend fun getRouteList(): Response<RouteList?>
}
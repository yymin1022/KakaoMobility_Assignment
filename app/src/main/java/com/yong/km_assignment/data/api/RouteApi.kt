package com.yong.km_assignment.data.api

import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.data.model.RouteList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RouteApi {
    @Headers(
        "Authorization: API_KEY",
        "Content-Type: application/json")
    @GET("")
    suspend fun getRouteDetail(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<RouteDetail>

    @Headers(
        "Authorization: API_KEY",
        "Content-Type: application/json")
    @GET("")
    suspend fun getRouteInfo(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<RouteInfo>

    @Headers("Authorization: API_KEY")
    @GET("")
    suspend fun getRouteList(): Response<RouteList>
}
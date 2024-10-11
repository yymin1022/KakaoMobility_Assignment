package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class RouteInfo(
    @SerializedName("distance")
    val routeDistance: Int,
    @SerializedName("time")
    val routeTime: Int
)
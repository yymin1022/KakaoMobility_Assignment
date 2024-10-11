package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class RouteInfo(
    @SerializedName("routeDistance")
    val distance: Int,
    @SerializedName("routeTime")
    val time: Int
)
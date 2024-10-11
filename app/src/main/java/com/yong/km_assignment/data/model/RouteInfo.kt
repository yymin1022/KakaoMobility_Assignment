package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class ApiRouteInfo(
    @SerializedName("routeDistance")
    val distance: Int,
    @SerializedName("routeTime")
    val time: Int
)
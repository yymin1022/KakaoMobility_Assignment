package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class RouteDetail(
    @SerializedName("points")
    val routePointList: String,
    @SerializedName("traffic_state")
    val routeTraffic: String
)
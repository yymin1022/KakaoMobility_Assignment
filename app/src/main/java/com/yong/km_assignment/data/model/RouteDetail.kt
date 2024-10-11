package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class RouteDetail(
    @SerializedName("points")
    val routePointList: String,
    @SerializedName("traffic_state")
    val routeTraffic: String
)

enum class TrafficState {
    TRAFFIC_UNKNOWN,
    TRAFFIC_JAM,
    TRAFFIC_DELAY,
    TRAFFIC_SLOW,
    TRAFFIC_NORMAL,
    TRAFFIC_BLOCK
}
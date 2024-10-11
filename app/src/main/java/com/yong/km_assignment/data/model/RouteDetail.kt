package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class ApiRouteDetail(
    @SerializedName("routePointList")
    val points: String,
    @SerializedName("routeTraffic")
    val traffic_state: String
)

enum class TrafficState {
    TRAFFIC_UNKNOWN,
    TRAFFIC_JAM,
    TRAFFIC_DELAY,
    TRAFFIC_SLOW,
    TRAFFIC_NORMAL,
    TRAFFIC_BLOCK
}
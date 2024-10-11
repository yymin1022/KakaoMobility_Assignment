package com.yong.km_assignment.data.model

data class RouteDetail(
    val status: ApiResult,
    val partList: List<RouteDetailPart>
)

data class RouteDetailPart(
    val pointList: List<RouteDetailPoint>,
    val trafficState: TrafficState
)

data class RouteDetailPoint(
    val lat: Double,
    val lng: Double
)

enum class TrafficState {
    TRAFFIC_UNKNOWN,
    TRAFFIC_JAM,
    TRAFFIC_DELAY,
    TRAFFIC_SLOW,
    TRAFFIC_NORMAL,
    TRAFFIC_BLOCK
}
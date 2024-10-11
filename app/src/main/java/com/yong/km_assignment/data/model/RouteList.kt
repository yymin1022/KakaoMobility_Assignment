package com.yong.km_assignment.data.model

data class RouteList(
    val status: ApiResult,
    val routeList: List<RouteListItem>
)

data class RouteListItem(
    val routeFrom: String,
    val routeTo: String
)

package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class RouteList(
    @SerializedName("locations")
    val routeList: List<RouteListItem>
)

data class RouteListItem(
    @SerializedName("origin")
    val routeFrom: String,
    @SerializedName("destination")
    val routeTo: String
)
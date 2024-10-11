package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class RouteList(
    @SerializedName("routeList")
    val locations: List<RouteListItem>
)

data class RouteListItem(
    @SerializedName("routeFrom")
    val origin: String,
    @SerializedName("routeTo")
    val destination: String
)
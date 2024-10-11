package com.yong.km_assignment.data.model

import com.google.gson.annotations.SerializedName

data class ApiRouteList(
    @SerializedName("routeList")
    val locations: List<ApiRouteListItem>
)

data class ApiRouteListItem(
    @SerializedName("routeFrom")
    val origin: String,
    @SerializedName("routeTo")
    val destination: String
)
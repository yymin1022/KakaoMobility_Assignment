package com.yong.km_assignment.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class RouteDetail(
    @SerializedName("points")
    val routePointList: String,
    @SerializedName("traffic_state")
    val routeTraffic: String
): Parcelable {
    constructor(parcel: Parcel): this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(routePointList)
        dest.writeString(routeTraffic)
    }

    companion object CREATOR : Parcelable.Creator<RouteDetail> {
        override fun createFromParcel(parcel: Parcel): RouteDetail {
            return RouteDetail(parcel)
        }

        override fun newArray(size: Int): Array<RouteDetail?> {
            return arrayOfNulls(size)
        }
    }
}
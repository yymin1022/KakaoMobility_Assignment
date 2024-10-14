package com.yong.km_assignment.ui.mapview

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yong.km_assignment.R
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.ui.common.StatusTextView

@Composable
fun RouteInfoView(
    routeInfo: RouteInfo?,
    context: Context,
    modifier: Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column {
            if (routeInfo != null) {
                Text(
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp),
                    fontSize = 20.sp,
                    text = context.getString(R.string.mapview_info_time).format(
                        routeInfo.routeTime / 3600,
                        (routeInfo.routeTime % 3600) / 60
                    )
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 20.dp),
                    fontSize = 20.sp,
                    text = context.getString(R.string.mapview_info_distance).format(
                        routeInfo.routeDistance / 1000f
                    )
                )
            } else {
                StatusTextView(
                    modifier = Modifier.padding(all = 20.dp),
                    msg = context.getString(R.string.mapview_error_info_api),
                )
            }
        }
    }
}
package com.yong.km_assignment.ui.mapview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.ui.common.StatusTextView

@Composable
fun RouteInfoView(
    routeInfo: RouteInfo?,
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
                    text = "시간: %d시간 %d분".format(
                        routeInfo.routeTime / 3600,
                        (routeInfo.routeTime % 3600) / 60
                    )
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 20.dp),
                    text = "거리: %,.1fkm".format(routeInfo.routeDistance / 1000f),
                    fontSize = 20.sp
                )
            } else {
                StatusTextView(
                    modifier = Modifier.padding(all = 20.dp),
                    msg = "경로 시간/거리 정보를 불러오지 못했습니다.",
                )
            }
        }
    }
}
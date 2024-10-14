package com.yong.km_assignment.ui.main

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yong.km_assignment.R
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.model.RouteListItem
import com.yong.km_assignment.ui.common.StatusTextView
import java.util.Locale

@Composable
fun RouteListView(
    routeList: RouteList?,
    routeListLoaded: Boolean,
    errCode: Int,
    errMessage: String,
    context: Context,
    modifier: Modifier,
    onRouteItemClick: (String, String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (routeListLoaded) {
            if (routeList != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(routeList.routeList) { route ->
                        RouteListItem(route = route, context = context, onClick = onRouteItemClick)
                    }
                }
            } else{
                StatusTextView(
                    modifier = Modifier.align(Alignment.Center),
                    msg = context.getString(R.string.main_error_api).format(Locale.getDefault(), errCode, errMessage)
                )
            }
        } else {
            StatusTextView(
                modifier = Modifier.align(Alignment.Center),
                msg = context.getString(R.string.main_loading)
            )
        }
    }
}

@Composable
fun RouteListItem(
    route: RouteListItem,
    context: Context,
    onClick: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(route.routeFrom, route.routeTo) }
            .padding(16.dp)
    ) {
        Text(text = context.getString(R.string.main_list_item_from).format(route.routeFrom))
        Text(text = context.getString(R.string.main_list_item_to).format(route.routeTo))
    }
}
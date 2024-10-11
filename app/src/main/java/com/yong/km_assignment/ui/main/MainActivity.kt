package com.yong.km_assignment.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.model.RouteListItem
import com.yong.km_assignment.ui.mapview.MapviewActivity
import com.yong.km_assignment.ui.theme.KakaoMobility_AssignmentTheme

class MainActivity: ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KakaoMobility_AssignmentTheme {
                val routeDetail = viewModel.routeDetail.observeAsState()
                val routeList = viewModel.routeList.observeAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    routeList.value?.let {
                        RouteList(
                            routeList = it,
                            modifier = Modifier.padding(innerPadding),
                            onRouteItemClick = { route -> viewModel.getRouteDetail(route) }
                        )
                    }
                }

                if(routeDetail.value != null) {
                    val intent = Intent(applicationContext, MapviewActivity::class.java)
                    intent.putExtra("RouteDetail", routeDetail.value?.toTypedArray())
                    startActivity(intent)
                } else {
                    Log.d("Route List", "Route is Null")
                }
            }
        }

        viewModel.getRouteList()
    }
}

@Composable
fun RouteList(
    routeList: RouteList,
    modifier: Modifier = Modifier,
    onRouteItemClick: (RouteListItem) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(routeList.routeList) { route ->
            RouteItem(route = route, onClick = onRouteItemClick)
        }
    }
}

@Composable
fun RouteItem(
    route: RouteListItem,
    onClick: (RouteListItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(route) }
            .padding(16.dp)
    ) {
        Text(text = "Origin: ${route.routeFrom}")
        Text(text = "Destination: ${route.routeTo}")
    }
}
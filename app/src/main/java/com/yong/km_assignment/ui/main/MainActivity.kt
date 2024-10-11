package com.yong.km_assignment.ui.main

import android.content.Intent
import android.os.Bundle
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
                val routeList = viewModel.routeList.observeAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    routeList.value?.let {
                        RouteList(
                            routeList = it,
                            modifier = Modifier.padding(innerPadding),
                            onRouteItemClick = { route ->
                                viewModel.onRouteItemClick(route) { res ->
                                    if(res) {
                                        startActivity(Intent(applicationContext, MapviewActivity::class.java))
                                    }
                                }
                            }
                        )
                    }
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
            .padding(16.dp)
            .clickable {
                onClick(route)
            }
    ) {
        Text(text = "Origin: ${route.routeFrom}")
        Text(text = "Destination: ${route.routeTo}")
    }
}
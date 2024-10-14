package com.yong.km_assignment.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.model.RouteListItem
import com.yong.km_assignment.ui.mapview.MapviewActivity
import com.yong.km_assignment.ui.theme.KakaoMobilityAssignmentTheme
import com.yong.km_assignment.util.NetworkUtil

class MainActivity: ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KakaoMobilityAssignmentTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if(!NetworkUtil.isNetworkAvailable(applicationContext)) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 20.sp,
                                text = "네트워크 연결을 확인해주세요."
                            )
                        }
                        return@Scaffold
                    }

                    val routeListLoaded = viewModel.routeListLoaded.collectAsState()
                    viewModel.getRouteList()
                    routeListLoaded.value.let {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            if (it) {
                                val routeList = viewModel.routeList
                                if (routeList != null) {
                                    RouteList(
                                        routeList = routeList,
                                        modifier = Modifier.fillMaxSize(),
                                        onRouteItemClick = { routeFrom, routeTo ->
                                            val intent = Intent(
                                                applicationContext,
                                                MapviewActivity::class.java
                                            )
                                            intent.putExtra("routeFrom", routeFrom)
                                            intent.putExtra("routeTo", routeTo)
                                            startActivity(intent)
                                        }
                                    )
                                } else{
                                    Text(
                                        modifier = Modifier.align(Alignment.Center),
                                        fontSize = 20.sp,
                                        text = "경로 목록을 불러오지 못했습니다.\n오류코드: ${viewModel.errCode} (${viewModel.errMessage})"
                                    )
                                }
                            } else {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 20.sp,
                                    text = "경로 목록을 불러오는 중..."
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RouteList(
    routeList: RouteList,
    modifier: Modifier = Modifier,
    onRouteItemClick: (String, String) -> Unit
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
    onClick: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(route.routeFrom, route.routeTo) }
            .padding(16.dp)
    ) {
        Text(text = "출발지: ${route.routeFrom}")
        Text(text = "도착지: ${route.routeTo}")
    }
}
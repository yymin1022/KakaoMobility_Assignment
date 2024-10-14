package com.yong.km_assignment.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yong.km_assignment.ui.common.StatusTextView
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
                            StatusTextView(
                                modifier = Modifier.align(Alignment.Center),
                                msg = "네트워크 연결을 확인해주세요."
                            )
                        }
                        return@Scaffold
                    }

                    val routeListLoaded = viewModel.routeListLoaded.collectAsState()
                    viewModel.getRouteList()
                    RouteListView(
                        routeList = viewModel.routeList,
                        routeListLoaded = routeListLoaded.value,
                        errCode = viewModel.errCode,
                        errMessage = viewModel.errMessage,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
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
                }
            }
        }
    }
}
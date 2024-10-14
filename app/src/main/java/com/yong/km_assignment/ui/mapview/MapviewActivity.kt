package com.yong.km_assignment.ui.mapview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yong.km_assignment.ui.common.StatusTextView
import com.yong.km_assignment.ui.theme.KakaoMobilityAssignmentTheme
import com.yong.km_assignment.util.NetworkUtil

class MapviewActivity: ComponentActivity() {
    private val viewModel: MapviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            KakaoMobilityAssignmentTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (!NetworkUtil.isNetworkAvailable(applicationContext)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            StatusTextView(
                                modifier = Modifier.align(Alignment.Center),
                                msg = "네트워크 연결을 확인해주세요."
                            )
                        }
                        return@Scaffold
                    }

                    val routeFrom = intent.getStringExtra("routeFrom") ?: ""
                    val routeTo = intent.getStringExtra("routeTo") ?: ""
                    viewModel.getRouteDetailAndInfo(routeFrom, routeTo)

                    val uiState = viewModel.uiState.collectAsState()
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val state = uiState.value) {
                            is UIState.Loading -> {
                                StatusTextView(
                                    modifier = Modifier.align(Alignment.Center),
                                    msg = "경로 정보를 불러오는 중..."
                                )
                            }
                            is UIState.Success -> {
                                MapviewKakaoMap(
                                    routeDetail = state.routeDetail,
                                    modifier = Modifier
                                )
                                RouteInfoView(
                                    routeInfo = state.routeInfo,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 50.dp)
                                )
                            }
                            is UIState.Error -> {
                                StatusTextView(
                                    modifier = Modifier.align(Alignment.Center),
                                    msg = "경로 정보를 불러오지 못했습니다.\n오류코드: ${state.errorCode} (${state.errorMessage})"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
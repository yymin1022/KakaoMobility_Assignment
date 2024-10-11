package com.yong.km_assignment.ui.mapview

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.ui.theme.KakaoMobility_AssignmentTheme
import com.yong.km_assignment.ui.theme.RouteBlock
import com.yong.km_assignment.ui.theme.RouteDelay
import com.yong.km_assignment.ui.theme.RouteJam
import com.yong.km_assignment.ui.theme.RouteNormal
import com.yong.km_assignment.ui.theme.RouteSlow
import com.yong.km_assignment.ui.theme.RouteUnknown


class MapviewActivity: ComponentActivity() {
    private val viewModel: MapviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KakaoMobility_AssignmentTheme {
                val routeDetail = viewModel.routeDetail.observeAsState()
                routeDetail.value?.let {
                    KakaoMapView(routeDetail = it, modifier = Modifier)
                }
            }
        }

        val routeDetail: List<RouteDetail>? = intent.getParcelableArrayListExtra("RouteDetail")
        if(routeDetail != null) {
            viewModel.setRouteDetail(routeDetail)
        }
    }
}

@Composable
fun KakaoMapView(routeDetail: List<RouteDetail>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            val mapView = MapView(context)
            mapView.start(
                object: MapLifeCycleCallback() {
                    override fun onMapDestroy() {
                        Log.d("KakaoMapView", "Map Destroyed")
                    }

                    override fun onMapError(error: Exception) {
                        Log.e("KakaoMapView", "Map Error: ${error.message}")
                    }
                },
                object: KakaoMapReadyCallback() {
                    override fun onMapReady(kakaoMap: KakaoMap) {
                        Log.d("KakaoMapView", "Map is Ready")

                        val mapLayer = kakaoMap.routeLineManager!!.layer
                        val routeStyleNormal: RouteLineStylesSet = RouteLineStylesSet.from(
                            "RouteLine",
                            RouteLineStyles.from(RouteLineStyle.from(15f, RouteUnknown.toArgb())),
                            RouteLineStyles.from(RouteLineStyle.from(15f, RouteJam.toArgb())),
                            RouteLineStyles.from(RouteLineStyle.from(15f, RouteDelay.toArgb())),
                            RouteLineStyles.from(RouteLineStyle.from(15f, RouteSlow.toArgb())),
                            RouteLineStyles.from(RouteLineStyle.from(15f, RouteNormal.toArgb())),
                            RouteLineStyles.from(RouteLineStyle.from(15f, RouteBlock.toArgb()))
                        )

                        routeDetail.forEach { routeDetailItem ->
                            val routePointList = routeDetailItem.routePointList.split(" ").map {
                                val pointPair = it.split(",")
                                LatLng.from(pointPair[1].toDouble(), pointPair[0].toDouble())
                            }

                            val mapRouteSegment = RouteLineSegment.from(routePointList)
                                .setStyles(
                                    when (routeDetailItem.routeTraffic) {
                                        "UNKNOWN" -> routeStyleNormal.getStyles(0)
                                        "JAM" -> routeStyleNormal.getStyles(1)
                                        "DELAY" -> routeStyleNormal.getStyles(2)
                                        "SLOW" -> routeStyleNormal.getStyles(3)
                                        "NORMAL" -> routeStyleNormal.getStyles(4)
                                        else -> routeStyleNormal.getStyles(5)
                                    }
                                )
                            val options = RouteLineOptions.from(mapRouteSegment)
                            mapLayer.addRouteLine(options)
                        }
                    }
                }
            )

            return@AndroidView mapView
        },
        modifier = modifier.fillMaxSize()
    )
}
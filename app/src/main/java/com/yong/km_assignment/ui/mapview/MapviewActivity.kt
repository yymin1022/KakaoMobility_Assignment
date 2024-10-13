package com.yong.km_assignment.ui.mapview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition
import com.kakao.vectormap.camera.CameraUpdateFactory.zoomTo
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.label.LabelTextStyle
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteInfo
import com.yong.km_assignment.ui.theme.KakaoMobility_AssignmentTheme
import com.yong.km_assignment.ui.theme.*
import com.yong.km_assignment.util.LogUtil.LogD

class MapviewActivity: ComponentActivity() {
    private val viewModel: MapviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val routeFrom = intent.getStringExtra("routeFrom") ?: ""
        val routeTo = intent.getStringExtra("routeTo") ?: ""

        enableEdgeToEdge()
        setContent {
            KakaoMobility_AssignmentTheme {
                val routeDetailLoaded = viewModel.routeDetailLoaded.observeAsState()
                val routeInfo = viewModel.routeInfo.observeAsState()
                routeDetailLoaded.value.let {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (it == true) {
                            val routeDetail = viewModel.routeDetail
                            if (routeDetail != null) {
                                KakaoMapView(
                                    routeDetail = routeDetail,
                                    modifier = Modifier
                                )
                                Card(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 50.dp)
                                ) {
                                    viewModel.getRouteInfo(routeFrom, routeTo)
                                    RouteInfoView(routeInfo = routeInfo.value)
                                }
                            } else {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 20.sp,
                                    text = "경로 정보를 불러오지 못했습니다.\n오류코드: ${viewModel.errCode} (${viewModel.errMessage})"
                                )
                            }
                        } else {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 20.sp,
                                text = "경로 정보를 불러오는 중..."
                            )
                        }
                    }
                }
            }
        }

        viewModel.getRouteDetail(routeFrom, routeTo)
    }
}

@Composable
fun RouteInfoView(
    routeInfo: RouteInfo?
) {
    Column {
        if (routeInfo != null) {
            Text(
                modifier = Modifier.padding(start = 20.dp, top = 20.dp),
                fontSize = 20.sp,
                text = "시간: %d시간 %d분".format(routeInfo.routeTime / 3600, (routeInfo.routeTime % 3600) / 60)
            )
            Text(
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 20.dp),
                text = "거리: %,.1fkm".format(routeInfo.routeDistance / 1000f),
                fontSize = 20.sp
            )
        } else {
            Text(
                modifier = Modifier.padding(all = 20.dp),
                text = "경로 시간/거리 정보를 불러오지 못했습니다.",
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun KakaoMapView(
    routeDetail: List<RouteDetail>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            val mapView = MapView(context)
            mapView.start(
                object: MapLifeCycleCallback() {
                    override fun onMapDestroy() {
                        LogD("KakaoMapView", "Map Destroyed")
                    }

                    override fun onMapError(error: Exception) {
                        LogD("KakaoMapView", "Map Error: ${error.message}")
                    }
                },
                object: KakaoMapReadyCallback() {
                    override fun onMapReady(kakaoMap: KakaoMap) {
                        LogD("KakaoMapView", "Map is Ready")

                        addRouteLabelView(kakaoMap, routeDetail)
                        setKakaoMapCameraView(kakaoMap, routeDetail)
                        setKakaoMapRouteLine(kakaoMap, routeDetail)
                    }
                }
            )

            return@AndroidView mapView
        },
        modifier = modifier.fillMaxSize()
    )
}

fun addRouteLabelView(
    kakaoMap: KakaoMap,
    routeDetail: List<RouteDetail>
) {
    val latlngFrom = routeDetail.first().routePointList.split(" ")[0].split(",")
    val latlngTo = routeDetail.last().routePointList.split(" ")[0].split(",")
    val labelStyles = kakaoMap.labelManager!!.addLabelStyles(LabelStyles.from(LabelStyle.from(
        LabelTextStyle.from(30, Color.Black.toArgb()))))
    val labelOptionsFrom = LabelOptions.from(LatLng.from(latlngFrom[1].toDouble(), latlngFrom[0].toDouble())).let {
        it.setStyles(labelStyles)
        it.setTexts(LabelTextBuilder().setTexts("출발"))
    }
    val labelOptionsTo = LabelOptions.from(LatLng.from(latlngTo[1].toDouble(), latlngTo[0].toDouble())).let {
        it.setStyles(labelStyles)
        it.setTexts(LabelTextBuilder().setTexts("도착"))
    }
    kakaoMap.labelManager!!.layer?.let {
        it.addLabel(labelOptionsFrom)
        it.addLabel(labelOptionsTo)
    }
}

fun setKakaoMapCameraView(
    kakaoMap: KakaoMap,
    routeDetail: List<RouteDetail>
) {
    val latlngFrom = routeDetail.first().routePointList.split(" ")[0].split(",")
    val latlngTo = routeDetail.last().routePointList.split(" ")[0].split(",")
    val latlngCenter = LatLng.from((latlngFrom[1].toDouble() + latlngTo[1].toDouble()) / 2, (latlngFrom[0].toDouble() + latlngTo[0].toDouble()) / 2)
    kakaoMap.moveCamera(zoomTo(10))
    kakaoMap.moveCamera(newCenterPosition(latlngCenter), CameraAnimation.from(500, true, true))
}

fun setKakaoMapRouteLine(
    kakaoMap: KakaoMap,
    routeDetail: List<RouteDetail>
) {
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
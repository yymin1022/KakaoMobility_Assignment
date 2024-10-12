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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapGravity
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
import com.kakao.vectormap.mapwidget.MapWidgetOptions
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Horizontal
import com.kakao.vectormap.mapwidget.component.Orientation
import com.kakao.vectormap.mapwidget.component.Vertical
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.data.model.RouteInfo
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
                val routeInfo = viewModel.routeInfo.observeAsState()
                routeDetail.value?.let {
                    KakaoMapView(routeDetail = it, routeInfo = routeInfo.value, modifier = Modifier)
                }
            }
        }

        val routeFrom = intent.getStringExtra("routeFrom") ?: ""
        val routeTo = intent.getStringExtra("routeTo") ?: ""
        viewModel.getRouteDetail(routeFrom, routeTo)
        viewModel.getRouteInfo(routeFrom, routeTo)
    }
}

@Composable
fun KakaoMapView(
    routeDetail: List<RouteDetail>,
    routeInfo: RouteInfo?,
    modifier: Modifier = Modifier
) {
    Log.d("MapView", "${routeInfo?.routeTime} / ${routeInfo?.routeDistance}")
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

                        addRouteInfoView(kakaoMap, routeInfo!!)
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

fun addRouteInfoView(
    kakaoMap: KakaoMap,
    routeInfo: RouteInfo
) {
    val widgetLayout = GuiLayout(Orientation.Vertical)
    widgetLayout.setAlign(Vertical.Center, Horizontal.Left)
    widgetLayout.setPadding(50, 50, 50, 50)

    val widgetTextTime = GuiText("시간: %d시간 %d분".format(routeInfo.routeTime / 3600, (routeInfo.routeTime % 3600) / 60))
    widgetTextTime.setAlign(Vertical.Center, Horizontal.Left)
    widgetTextTime.setTextSize(40)
    widgetLayout.addView(widgetTextTime)
    val widgetTextDistance = GuiText("거리: %,.1fkm".format(routeInfo.routeDistance / 1000f))
    widgetTextDistance.setAlign(Vertical.Center, Horizontal.Left)
    widgetTextDistance.setTextSize(40)
    widgetLayout.addView(widgetTextDistance)

    val widgetOption = MapWidgetOptions.from("RouteInfo")
    widgetOption.setRootView(widgetLayout)

    kakaoMap.mapWidgetManager!!.mapWidgetLayer.let {
        it.addMapWidget(widgetOption)
        it.getMapWidget("RouteInfo")?.setPosition(MapGravity.BOTTOM or MapGravity.LEFT, 0f, 0f)
    }
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
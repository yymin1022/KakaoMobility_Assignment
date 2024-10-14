package com.yong.km_assignment.ui.mapview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
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
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.yong.km_assignment.R
import com.yong.km_assignment.data.model.RouteDetail
import com.yong.km_assignment.ui.theme.RouteBlock
import com.yong.km_assignment.ui.theme.RouteDelay
import com.yong.km_assignment.ui.theme.RouteJam
import com.yong.km_assignment.ui.theme.RouteNormal
import com.yong.km_assignment.ui.theme.RouteSlow
import com.yong.km_assignment.ui.theme.RouteUnknown
import com.yong.km_assignment.util.LogUtil.LogD

@Composable
fun MapviewKakaoMap(
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
    kakaoMap.labelManager!!.layer?.let {
        val latlngFrom = routeDetail.first().routePointList.split(" ")[0].split(",")
        val labelStylesFrom = kakaoMap.labelManager!!.addLabelStyles(
            LabelStyles.from(
            LabelStyle.from(R.drawable.ic_marker_from)))
        val labelOptionsFrom = LabelOptions.from(LatLng.from(latlngFrom[1].toDouble(), latlngFrom[0].toDouble())).let { option ->
            option.setStyles(labelStylesFrom)
            option.setTexts(LabelTextBuilder().setTexts("출발"))
        }
        it.addLabel(labelOptionsFrom)

        val latlngTo = routeDetail.last().routePointList.split(" ")[0].split(",")
        val labelStylesTo = kakaoMap.labelManager!!.addLabelStyles(
            LabelStyles.from(
            LabelStyle.from(R.drawable.ic_marker_to)))
        val labelOptionsTo = LabelOptions.from(LatLng.from(latlngTo[1].toDouble(), latlngTo[0].toDouble())).let { option ->
            option.setStyles(labelStylesTo)
            option.setTexts(LabelTextBuilder().setTexts("도착"))
        }
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
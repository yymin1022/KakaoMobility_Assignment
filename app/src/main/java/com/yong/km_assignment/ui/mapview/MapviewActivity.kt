package com.yong.km_assignment.ui.mapview

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.yong.km_assignment.data.model.RouteDetail
import java.lang.Exception

class MapviewActivity: ComponentActivity() {
    private val viewModel: MapviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KakaoMapView(modifier = Modifier)
        }

        val routeDetail: List<RouteDetail> = intent.getParcelableArrayListExtra<RouteDetail?>("RouteDetail")!!.toList()
        viewModel.setRouteDetail(routeDetail)
    }
}

@Composable
fun KakaoMapView(modifier: Modifier = Modifier) {
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
                    }
                }
            )
            mapView
        },
        modifier = modifier.fillMaxSize()
    )
}
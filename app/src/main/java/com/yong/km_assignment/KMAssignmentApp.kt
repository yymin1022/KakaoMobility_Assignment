package com.yong.km_assignment

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class KMAssignmentApp: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_SDK_KEY)
    }
}
package com.yong.km_assignment.util

import android.util.Log

object LogUtil {
    fun LogD(msg: String) {
        Log.d("KM Assignment", msg)
    }

    fun LogD(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun LogE(msg: String) {
        Log.e("KM Assignment", msg)
    }

    fun LogE(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    fun LogI(msg: String) {
        Log.i("KM Assignment", msg)
    }

    fun LogI(tag: String, msg: String) {
        Log.i(tag, msg)
    }
}
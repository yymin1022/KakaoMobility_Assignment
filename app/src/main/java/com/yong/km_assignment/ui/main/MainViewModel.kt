package com.yong.km_assignment.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.model.RouteListItem
import com.yong.km_assignment.data.repository.RouteDetailRepository
import com.yong.km_assignment.data.repository.RouteListRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val repositoryDetail = RouteDetailRepository()
    private val repositoryList = RouteListRepository()
    private val _routeList: MutableLiveData<RouteList> = MutableLiveData()
    val routeList: LiveData<RouteList> = _routeList

    fun getRouteList() {
        viewModelScope.launch {
            repositoryList.getRouteList().let {
                if(it != null) {
                    _routeList.postValue(it)
                }
            }
        }
    }

    fun onRouteItemClick(route: RouteListItem) {
        Log.d("RouteList", "Clicked ${route.routeFrom} -> ${route.routeTo}")

        viewModelScope.launch {
            repositoryDetail.getRouteDetail(route.routeFrom, route.routeTo).let {
                if(it != null) {
                    Log.d("RouteDetail", "Result is $it")
                }
            }
        }
    }
}
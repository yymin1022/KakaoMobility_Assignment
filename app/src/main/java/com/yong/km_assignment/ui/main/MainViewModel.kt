package com.yong.km_assignment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.km_assignment.data.model.RouteList
import com.yong.km_assignment.data.repository.RouteListRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _repositoryList = RouteListRepository()
    private val _routeListLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    var routeList: RouteList? = null
    val routeListLoaded: LiveData<Boolean> = _routeListLoaded
    var errCode: Int = 0
    var errMessage = "Success"

    fun getRouteList() {
        viewModelScope.launch {
            _repositoryList.getRouteList().let {
                routeList = it.body()
                if(it.body() == null) {
                    errCode = it.code()
                    errMessage = when(errCode) {
                        404 -> "Not Found"
                        500 -> "Server Error"
                        else -> "Unknown Error"
                    }
                }
                _routeListLoaded.postValue(true)
            }
        }
    }
}
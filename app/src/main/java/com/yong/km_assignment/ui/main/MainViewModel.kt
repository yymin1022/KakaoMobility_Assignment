package com.yong.km_assignment.ui.main

import androidx.lifecycle.ViewModel
import com.yong.km_assignment.data.repository.RouteListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RouteListRepository
): ViewModel() {
}
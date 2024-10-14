package com.yong.km_assignment.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ErrorTextView(modifier: Modifier, msg: String) {
    Text(
        modifier = modifier,
        fontSize = 20.sp,
        text = msg
    )
}
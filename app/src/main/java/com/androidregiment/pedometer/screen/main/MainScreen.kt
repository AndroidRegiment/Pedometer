package com.androidregiment.pedometer.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {

    val steps = viewModel.stepCount.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = steps.value.toString(), fontSize = 32.sp)
        Button(onClick = { viewModel.startCounting() }) {
            Text(text = "Start Counting")
        }
        Button(onClick = { viewModel.stopCounting() }) {
            Text(text = "Stop Counting")
        }
    }

}

package com.androidregiment.pedometer.screen.stepcounter

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidregiment.pedometer.screen.utils.Constant
import com.androidregiment.pedometer.service.ServiceHelper
import com.androidregiment.pedometer.service.StepCounterService
import com.androidregiment.pedometer.service.StepCounterState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StepCounterScreen(
    service: StepCounterService
) {
    val stepsCount by service.steps
    val serviceState by service.currentState
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceAround
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Text(text = "Total Steps....", fontSize = 42.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stepsCount.toString(), fontSize = 32.sp)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {


            AnimatedContent(targetState = serviceState) {
                Button(modifier =  Modifier.padding(16.dp), shape = RoundedCornerShape(percent = 50),onClick = {
                    if (serviceState == StepCounterState.Started.name) {
                        ServiceHelper.triggerForegroundService(
                            context = context, action = Constant.ACTION_SERVICE_STOP
                        )
                        Log.d("SERVICE STATE", serviceState)
                    } else {
                        ServiceHelper.triggerForegroundService(
                            context = context, action = Constant.ACTION_SERVICE_START
                        )
                        Log.d("SERVICE_STATE", serviceState)
                    }

                }) {
                    if (it == StepCounterState.Started.name) {
                        Text(text = "Stop Counting...")
                    } else {

                        Text(text = "Start Counting...")
                    }
                }

            }
        }
    }

}

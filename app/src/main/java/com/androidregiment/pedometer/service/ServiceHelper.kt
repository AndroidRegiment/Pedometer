package com.androidregiment.pedometer.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.androidregiment.pedometer.MainActivity
import com.androidregiment.pedometer.screen.utils.Constant.CLICK_REQUEST_CODE
import com.androidregiment.pedometer.screen.utils.Constant.STEP_COUNTER_STATE
import com.androidregiment.pedometer.screen.utils.Constant.STOP_REQUEST_CODE

object ServiceHelper {

    private val flag =
        PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(STEP_COUNTER_STATE, StepCounterState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }


    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, StepCounterService::class.java).apply {
            putExtra(STEP_COUNTER_STATE, StepCounterState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }


    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, StepCounterService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

}
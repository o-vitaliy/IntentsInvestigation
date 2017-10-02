package com.nullgr.investigation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nullgr.investigation.TrackingService
import com.nullgr.investigation.monitor.Tracker

class InternalBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       /* context.startService(TrackingService.getIntent(
                context,
                TrackingService.INTERNAl,
                intent.getIntExtra(TrackingService.COUNT, 0)
        ))*/
    }

    companion object {
        val ACTION = "action_internal"
    }
}
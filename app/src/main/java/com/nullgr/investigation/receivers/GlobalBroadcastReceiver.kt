package com.nullgr.investigation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nullgr.investigation.TrackingService

class GlobalBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       /* context.startService(TrackingService.getIntent(
                context,
                TrackingService.GLOBAL,
                intent.getIntExtra(TrackingService.COUNT, 0)
        ))*/
    }
    companion object {
        val ACTION = "com.nullgr.investigation.action"
    }
}

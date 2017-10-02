package com.nullgr.investigation

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.nullgr.investigation.monitor.Monitor
import com.nullgr.investigation.monitor.Tracker

class TrackingService : Service() {

    private val TRACK_DELAY = 500L

    private val binder = TrackingServiceBinder()

    var onTrackingDataChangeListener: OnTrackingDataChangeListener? = null

    private var trackerRunning = true
    var type: String? = null
    private var count: Int = 0
    private val trackerThread = Thread({
        while (trackerRunning) {
            val data = Monitor.getMotitoredData(this)
            onTrackingDataChangeListener?.onDataChanged(data)
            type?.let {
                val tracker = Tracker(it)
                tracker.track(data, this, count)
            }
            if (trackerRunning) {
                Thread.sleep(TRACK_DELAY)
            }
        }
    }
    )

    override fun onCreate() {
        super.onCreate()
        trackerThread.start()
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder;
    }

    inner class TrackingServiceBinder : Binder() {
        fun getService() = this@TrackingService
    }


    fun startTracking(count: Int) {
        trackerRunning = true
        this.count = count
    }

    fun stopTracking() {
        type = null
    }

    override fun onDestroy() {
        super.onDestroy()
        trackerRunning = false
    }

    companion object {
        val GLOBAL = "global"
        val LOCAL = "local"
        val INTERNAl = "internal"
        val SERVICE = "service"


        val TAG = TrackingService::class.java.canonicalName
        val TYPE = TAG + ".type"
        val COUNT = TAG + ".count"

        fun getIntent(context: Context, type: String, count: Int) =
                Intent(context, TrackingService::class.java).apply {
                    putExtra(TYPE, type)
                    putExtra(COUNT, count)

                }
    }

    interface OnTrackingDataChangeListener {
        fun onDataChanged(data: HashMap<String, String>)
    }

}

package com.nullgr.investigation

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.TextInputEditText
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.nullgr.investigation.monitor.Monitor
import com.nullgr.investigation.receivers.GlobalBroadcastReceiver
import com.nullgr.investigation.receivers.InternalBroadcastReceiver
import com.nullgr.investigation.receivers.LocalBroadcastReceiver
import com.nullgr.investigation.utils.toDecimalFormatted
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), TrackingService.OnTrackingDataChangeListener {

    var disposable: Disposable? = null
    var disposableRunAll: Disposable? = null

    var serviceConnection: ServiceConnection? = null

    var trackinService: TrackingService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        findViewById<View>(R.id.btn_send_into_broadcast).setOnClickListener(onStartClick)
        findViewById<View>(R.id.btn_send_into_local_broadcast).setOnClickListener(onStartClick)
        findViewById<View>(R.id.btn_send_into_service).setOnClickListener(onStartClick)
        findViewById<View>(R.id.btn_send_into_internal).setOnClickListener(onStartClick)

        findViewById<View>(R.id.btn_stop_sending).setOnClickListener {
            stopTrackingService()
            findViewById<View>(R.id.btn_stop_sending).visibility = View.GONE
        }

        findViewById<View>(R.id.btn_clear_logs).setOnClickListener {
            externalCacheDir.listFiles().forEach { it.delete() }
        }
        findViewById<View>(R.id.btn_run_all).setOnClickListener { runAllTypes() }
    }


    override fun onStart() {
        super.onStart()

        serviceConnection = ServiceConnection()
        bindService(Intent(this, TrackingService::class.java), serviceConnection, Service.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        stopTrackingService()

        serviceConnection?.let { unbindService(it) }
        serviceConnection = null
    }


    private val onStartClick = View.OnClickListener {
        startTrackingService()
        when (it.id) {
            R.id.btn_send_into_broadcast -> startSendingIntoBroadCast()
            R.id.btn_send_into_local_broadcast -> startSendingIntoLocalBroadcast()
            R.id.btn_send_into_internal -> startSendingIntoInternalBroadcast()
            R.id.btn_send_into_service -> startSendingIntoService()
        }
        findViewById<View>(R.id.btn_stop_sending).visibility = View.VISIBLE
    }


    private fun getIntentsCount() =
            findViewById<TextInputEditText>(R.id.edit_intents_count).
                    text?.
                    toString()?.
                    toInt() ?: 1

    private fun prepareObserver(): Observable<Intent> {
        return Producer.createObserver(getIntentsCount(), 1000)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
    }

    private fun startTrackingService() {
        trackinService?.startTracking(getIntentsCount())
    }

    override fun onDataChanged(data: HashMap<String, String>) {
        runOnUiThread({
            findViewById<TextView>(R.id.text_cpu_status).setText(data.get(Monitor.cpuUsedPercent)?.toDecimalFormatted())
            findViewById<TextView>(R.id.text_cpu_app).setText(data.get(Monitor.cpuAppUsedPercent)?.toDecimalFormatted())
            findViewById<TextView>(R.id.text_memory).setText(data.get(Monitor.memoryAppUsed)?.toDecimalFormatted())
        })
    }

    private fun stopTrackingService() {
        disposable?.dispose()
        disposable = null

        disposableRunAll?.dispose()
        disposableRunAll = null

        trackinService?.stopTracking()
    }

    private fun startSendingIntoBroadCast() {
        disposable = prepareObserver()
                .map {
                    it.apply {
                        action = GlobalBroadcastReceiver.ACTION
                    }
                }
                .subscribe(
                        {
                            trackinService?.type = (TrackingService.GLOBAL)
                            sendBroadcast(it)
                        }
                )
    }

    private fun startSendingIntoLocalBroadcast() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val localBroadcastReceiver = LocalBroadcastReceiver()
        val filter = IntentFilter(LocalBroadcastReceiver.ACTION)
        localBroadcastManager?.registerReceiver(localBroadcastReceiver, filter)

        disposable = prepareObserver()
                .map {
                    it.apply {
                        action = LocalBroadcastReceiver.ACTION
                    }
                }
                .subscribe(
                        {
                            trackinService?.type = (TrackingService.LOCAL)
                            localBroadcastManager?.sendBroadcast(it)
                        },
                        { /*error*/ },
                        {
                            localBroadcastManager.unregisterReceiver(localBroadcastReceiver)
                        }


                )
    }

    private fun startSendingIntoInternalBroadcast() {
        val internalBroadcastReceiver = InternalBroadcastReceiver()
        val filter = IntentFilter(InternalBroadcastReceiver.ACTION)
        registerReceiver(internalBroadcastReceiver, filter)

        disposable = prepareObserver()
                .map {
                    it.apply {
                        action = InternalBroadcastReceiver.ACTION
                    }
                }
                .subscribe(
                        {
                            trackinService?.type = (TrackingService.INTERNAl)
                            sendBroadcast(it)
                        },
                        { /*error*/ },
                        {
                            unregisterReceiver(internalBroadcastReceiver)
                        }
                )
    }

    private fun startSendingIntoService() {
        disposable = prepareObserver()
                .map {
                    it.apply {
                        component = ComponentName(this@MainActivity, TrackingService::class.java)
                        putExtra(TrackingService.TYPE, TrackingService.SERVICE)
                    }
                }
                .subscribe(
                        {
                            trackinService?.type = (TrackingService.SERVICE)
                            startService(it)
                        }
                )
    }

    private fun runAllTypes() {
        disposableRunAll = Observable.intervalRange(0, 5, 0, 15, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    trackinService?.startTracking(getIntentsCount())
                    Toast.makeText(this, "started", Toast.LENGTH_SHORT).show()
                    findViewById<View>(R.id.btn_stop_sending).visibility = View.VISIBLE
                }
                .doOnTerminate {
                    trackinService?.stopTracking()
                    Toast.makeText(this, "terminated", Toast.LENGTH_SHORT).show()
                    findViewById<View>(R.id.btn_stop_sending).visibility = View.GONE
                }
                .subscribe(
                        {
                            disposable?.dispose()
                            when (it) {
                                0L -> startSendingIntoBroadCast()
                                1L -> startSendingIntoLocalBroadcast()
                                2L -> startSendingIntoInternalBroadcast()
                                3L -> startSendingIntoService()
                            }
                            Toast.makeText(this, "type $it", Toast.LENGTH_SHORT).show()
                        }
                )
    }


    inner class ServiceConnection : android.content.ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            trackinService = null
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            trackinService = (p1 as TrackingService.TrackingServiceBinder).getService()
            trackinService?.onTrackingDataChangeListener = this@MainActivity
        }
    }

}

package nosaksh.me.forceeddystone.service

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import nosaksh.me.forceeddystone.R
import nosaksh.me.forceeddystone.util.EddystoneHelper
import nosaksh.me.forceeddystone.util.LogUtil
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

/**
 * Eddystone Central Service
 */
class EddystoneCentralService : Service() {

    companion object {

        const val FOREGROUND_NOTIFICATION_ID = 0x01

        var isRunning = false

        fun intent(context: Context) = Intent(context, EddystoneCentralService::class.java)

    }

    private val transmitter by lazy {
        val beaconParser = EddystoneHelper.buildEddystoneUrlBeaconParser()
        return@lazy BeaconTransmitter(this, beaconParser)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(FOREGROUND_NOTIFICATION_ID,
                NotificationCompat.Builder(this, "DEFAULT")
                        .setSmallIcon(R.mipmap.ic_stat_web)
                        .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                        .setAutoCancel(true)
                        .setContentTitle(getString(R.string.eddystone_central_foreground_service_title))
                        .setContentText(getString(R.string.eddystone_central_foreground_service_body))
                        .build())
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val beacon = EddystoneHelper.buildBeacon(this)
        if (beacon == null) {
            stopSelf()
        }
        if (BluetoothAdapter.getDefaultAdapter()?.isEnabled != true) {
            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }
        if (!transmitter.isStarted) {
            transmitter.stopAdvertising()
        }
        transmitter.startAdvertising(beacon, object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                super.onStartSuccess(settingsInEffect)
                LogUtil.d("start advertising success.")
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                LogUtil.d("start advertising failure.")
                transmitter.stopAdvertising()
                stopSelf()
            }

        })

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        transmitter.stopAdvertising()
        isRunning = false
        super.onDestroy()
    }

}

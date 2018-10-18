package nosaksh.me.forceeddystone.service

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.os.IBinder
import nosaksh.me.forceeddystone.R
import nosaksh.me.forceeddystone.domain.Configuration
import nosaksh.me.forceeddystone.util.LogUtil
import nosaksh.me.forceeddystone.util.NotificationHelper
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor
import java.net.MalformedURLException
import java.util.*

/**
 * Eddystone Central Service
 */
class EddystoneCentralService : Service() {

    companion object {

        const val FOREGROUND_NOTIFICATION_ID = 0x01

        var isRunning = false

        fun intent(context: Context) = Intent(context, EddystoneCentralService::class.java)

    }

    private val beaconTransmitter by lazy {
        val beaconParser = BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT)
        return@lazy BeaconTransmitter(this, beaconParser)
    }

    override fun onCreate() {
        super.onCreate()
        val notification = NotificationHelper.buildNotification(this,
                getString(R.string.eddystone_foreground_service_title),
                getString(R.string.eddystone_foreground_service_body))
        startForeground(FOREGROUND_NOTIFICATION_ID, notification)
        isRunning = true
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val beacon = buildBeacon()
        if (beacon == null) {
            stopSelf()
        }
        if (BluetoothAdapter.getDefaultAdapter()?.isEnabled != true) {
            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }
        if (!beaconTransmitter.isStarted) {
            beaconTransmitter.stopAdvertising()
        }
        beaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                super.onStartSuccess(settingsInEffect)
                LogUtil.d("start advertising success.")
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                LogUtil.d("start advertising failure.")
                beaconTransmitter.stopAdvertising()
                stopSelf()
            }

        })

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        beaconTransmitter.stopAdvertising()
        isRunning = false
        super.onDestroy()
    }


    private fun buildBeacon(): Beacon? {
        val webUrl = Configuration.getPhysicalWebUrl(this) ?: return null
        try {
            val urlBytes = UrlBeaconUrlCompressor.compress("https://$webUrl")
            val encodedUrlIdentifier = Identifier.fromBytes(urlBytes, 0, urlBytes.size, false)
            val identifiers = ArrayList<Identifier>()
            identifiers.add(encodedUrlIdentifier)

            return Beacon.Builder()
                    .setIdentifiers(identifiers)
                    .setManufacturer(0x0118)
                    .setTxPower(-59)
                    .build()
        } catch (e: MalformedURLException) {
            LogUtil.e(e)
            return null
        }
    }

}

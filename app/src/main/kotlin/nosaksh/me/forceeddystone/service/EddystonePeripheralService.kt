package nosaksh.me.forceeddystone.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import nosaksh.me.forceeddystone.R
import nosaksh.me.forceeddystone.util.EddystoneHelper
import nosaksh.me.forceeddystone.util.LogUtil
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Eddystone Peripheral Service
 */
class EddystonePeripheralService : Service(), BeaconConsumer, RangeNotifier {

    companion object {

        const val FOREGROUND_NOTIFICATION_ID = 0x02

        val TIMEOUT_AFTER_NOTIFICATION = TimeUnit.MINUTES.toMillis(1)

        var isRunning = false

        fun intent(context: Context) = Intent(context, EddystonePeripheralService::class.java)

    }

    private lateinit var manager: BeaconManager

    private val parser by lazy {
        return@lazy EddystoneHelper.buildEddystoneUrlBeaconParser()
    }

    private val region by lazy {
        return@lazy Region(UUID.randomUUID().toString(), null, null, null)
    }

    private val notificationManager by lazy {
        NotificationManagerCompat.from(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(FOREGROUND_NOTIFICATION_ID,
                NotificationCompat.Builder(this, "DEFAULT")
                        .setSmallIcon(R.mipmap.ic_stat_web)
                        .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                        .setAutoCancel(true)
                        .setContentTitle(getString(R.string.eddystone_peripheral_foreground_service_title))
                        .setContentText(getString(R.string.eddystone_peripheral_foreground_service_body))
                        .build())
        isRunning = true
        manager = BeaconManager.getInstanceForApplication(this).apply {
            beaconParsers.add(parser)
            addRangeNotifier(this@EddystonePeripheralService)
            backgroundMode = true
        }
        manager.bind(this)
    }

    override fun onDestroy() {
        manager.unbind(this)
        super.onDestroy()
    }

    override fun onBeaconServiceConnect() {
        manager.startRangingBeaconsInRegion(region)
    }

    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
        beacons?.forEach {
            notifyRangeBeacon(it)
        }
    }

    private fun notifyRangeBeacon(beacon: Beacon) {
        if (beacon.serviceUuid != 0xfeaa || beacon.beaconTypeCode != 0x10) return
        val url = UrlBeaconUrlCompressor.uncompress(beacon.id1.toByteArray())
        val body = "rssi:${beacon.rssi}, distance:=${String.format("%.2f", beacon.distance)}, address:=${beacon.bluetoothAddress}"
        val intent = PendingIntent.getActivity(this, 0, Intent(Intent.ACTION_VIEW, Uri.parse(url)), PendingIntent.FLAG_UPDATE_CURRENT)
        val notification =
                NotificationCompat.Builder(this, "DEFAULT")
                        .setContentIntent(intent)
                        .setSmallIcon(R.mipmap.ic_stat_signal_enter)
                        .setColor(ContextCompat.getColor(this, R.color.colorNotificationExitBeacon))
                        .setTimeoutAfter(TIMEOUT_AFTER_NOTIFICATION) // 通知のタイムアウトを設けることで以降のBeacon受信が発生しなくなった = Exit Beaconとみなす
                        .setContentTitle(url)
                        .setContentText(body)
                        .build()
        val id = ByteBuffer.wrap(beacon.id1.toByteArray()).int
        notificationManager.notify(id, notification)
        LogUtil.d("url:=$url")
    }

}

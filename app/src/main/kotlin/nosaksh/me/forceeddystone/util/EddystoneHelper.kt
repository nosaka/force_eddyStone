package nosaksh.me.forceeddystone.util

import android.content.Context
import nosaksh.me.forceeddystone.domain.Configuration
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor
import java.net.MalformedURLException
import java.util.*

/**
 *
 */
object EddystoneHelper {

    private const val MANUFACTURER = 0x00E0 // Google Inc.

    private const val TX_POWER = -59

    fun buildBeacon(context: Context): Beacon? {
        val webUrl = Configuration.getPhysicalWebUrl(context) ?: return null
        return try {
            val urlBytes = UrlBeaconUrlCompressor.compress("https://$webUrl")
            val encodedUrlIdentifier = Identifier.fromBytes(urlBytes, 0, urlBytes.size, false)
            val identifiers = ArrayList<Identifier>()
            identifiers.add(encodedUrlIdentifier)

            Beacon.Builder()
                    .setIdentifiers(identifiers)
                    .setManufacturer(MANUFACTURER)
                    .setTxPower(TX_POWER)
                    .build()
        } catch (e: MalformedURLException) {
            LogUtil.e(e)
            null
        }
    }

    fun buildEddystoneUrlBeaconParser(): BeaconParser = BeaconParser()
            .setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT)
}

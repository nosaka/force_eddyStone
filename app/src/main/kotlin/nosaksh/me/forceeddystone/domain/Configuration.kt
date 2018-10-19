package nosaksh.me.forceeddystone.domain

import android.content.Context
import nosaksh.me.forceeddystone.util.PreferencesHelper

/**
 * Configuration
 */
object Configuration : PreferencesHelper {

    private const val KEY_BOOT_EDDYSTONE_CENTRAL = "BOOT_EDDYSTONE_CENTRAL"

    private const val KEY_BOOT_PERIPHERAL_EDDYSTONE = "BOOT_PERIPHERAL_EDDYSTONE"

    private const val KEY_BOOT_PHYSICAL_WEB_URL = "PHYSICAL_WEB_URL"

    override val preferencesName: String = "configuration"

    fun removeBootEddystoneCentral(context: Context) {
        super.remove(context, KEY_BOOT_EDDYSTONE_CENTRAL)
    }

    fun saveBootEddystoneCentral(context: Context, value: Boolean) {
        super.putBoolean(context, KEY_BOOT_EDDYSTONE_CENTRAL, value)
    }

    fun getBootEddystoneCentral(context: Context): Boolean {
        return super.getBoolean(context, KEY_BOOT_EDDYSTONE_CENTRAL, false)
    }

    fun removeBootEddystonePeripheral(context: Context) {
        super.remove(context, KEY_BOOT_PERIPHERAL_EDDYSTONE)
    }

    fun saveBootEddystonePeripheral(context: Context, value: Boolean) {
        super.putBoolean(context, KEY_BOOT_PERIPHERAL_EDDYSTONE, value)
    }

    fun getBootEddystonePeripheral(context: Context): Boolean {
        return super.getBoolean(context, KEY_BOOT_PERIPHERAL_EDDYSTONE, false)
    }

    fun removePhysicalWebUrl(context: Context) {
        super.remove(context, KEY_BOOT_PHYSICAL_WEB_URL)
    }

    fun savePhysicalWebUrl(context: Context, value: String?) {
        super.putString(context, KEY_BOOT_PHYSICAL_WEB_URL, value)
    }

    fun getPhysicalWebUrl(context: Context): String? {
        return super.getString(context, KEY_BOOT_PHYSICAL_WEB_URL, null)
    }

}

package nosaksh.me.forceeddystone.domain

import android.content.Context
import nosaksh.me.forceeddystone.util.PreferencesHelper

/**
 * Configuration
 */
object Configuration : PreferencesHelper {

    private const val KEY_BOOT_EDDYSTONE = "BOOT_EDDYSTONE"

    private const val KEY_BOOT_PHYSICAL_WEB_URL = "PHYSICAL_WEB_URL"

    override val preferencesName: String = "configuration"

    fun removeBootEddystone(context: Context) {
        super.remove(context, KEY_BOOT_EDDYSTONE)
    }

    fun saveBootEddystone(context: Context, value: Boolean) {
        super.putBoolean(context, KEY_BOOT_EDDYSTONE, value)
    }

    fun getBootEddystone(context: Context): Boolean {
        return super.getBoolean(context, KEY_BOOT_EDDYSTONE, false)
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

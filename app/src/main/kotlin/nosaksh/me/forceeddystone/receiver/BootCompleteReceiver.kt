package nosaksh.me.forceeddystone.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nosaksh.me.forceeddystone.domain.Configuration
import nosaksh.me.forceeddystone.service.EddystoneCentralService

/**
 * Boot Complete Receiver
 */
class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "android.intent.action.BOOT_COMPLETED" -> {
                context ?: return
                if (Configuration.getBootEddystone(context)) {
                    context.startService(EddystoneCentralService.intent(context))
                } else {
                    context.stopService(EddystoneCentralService.intent(context))
                }
            }
            else -> Unit
        }
    }

}
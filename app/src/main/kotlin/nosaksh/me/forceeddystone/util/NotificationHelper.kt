package nosaksh.me.forceeddystone.util

import android.app.Notification
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import nosaksh.me.forceeddystone.R

/**
 * Notification Helper
 */
object NotificationHelper {

    private const val CHANNEL_ID_DEFAULT = "DEFAULT"

    /**
     * 通知作成処理
     */
    fun buildNotification(context: Context, title: String, body: String): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID_DEFAULT)
                .setSmallIcon(R.mipmap.ic_stat_web)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body)

        return builder.build()

    }


}


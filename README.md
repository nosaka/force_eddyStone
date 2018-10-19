# force-eddystone

任意のURLでEddystone-URLのビーコンを発信します。
このアドバタイズを受信したPhysical Web対応のAndroid端末はNearby Messagesとして通知を受け取ります。

## Description

### Eddystone Central
Androidを使用して、このアプリケーションのスイッチをオンにすると入力したURLのEddystoneビーコンのアドバタイズを発信します。
このアドバタイズを受信可能な端末はURLのPhysical Webとして通知を受信することができます。
URLの長さはURLスキームを除く17バイトまでとなりますので、短縮URLを使用することを推奨しています。

### Eddystone Peripheral
付近にある、Eddystoneフォーマットのビーコンのアドバタイズを通知します。
URL別に通知が管理され、1分間のアドバタイズの受信がない場合は通知が自動的にキャンセルされます。


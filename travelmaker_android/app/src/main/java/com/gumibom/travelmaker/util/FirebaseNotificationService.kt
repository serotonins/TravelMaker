package com.gumibom.travelmaker.util

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gumibom.travelmaker.ui.main.MainActivity

private const val TAG = "FirebaseNotify"
class FirebaseNotificationService : FirebaseMessagingService() {

    //새로운 토큰이 생성될 때마다 해당 콜백이 호출된다.
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate: aSDFASD")
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: ${token.toString()}")
//        sendRegistrationToServer()
    }
    private fun sendRegistrationToServer(){

    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "From: ${message.from}")
        //메시지를 받았을 때 처리한다.
        var messageTitle = "title";
        var messageContent = "content";
        if (message.notification!=null){
            //foreground이 때 받은 메시지
            messageTitle= message.notification!!.title.toString()
            messageContent = message.notification!!.body.toString()
            Log.d(TAG, "data.message1: ${messageTitle}")
            Log.d(TAG, "data.message2: ${messageTitle}")
            Log.d(TAG, "data.message3: ${messageContent}")
        }
        if (message.data!=null){//백그라운드
            var data = message.data
            Log.d(TAG, "data.message: ${data.get("title")}")
            Log.d(TAG, "data.message: ${data.get("body")}")
            messageTitle = data.get("title").toString()
            messageContent = data.get("body").toString()
        }

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // 알림 클릭 시 NotificationFragment를 열기 위한 추가 정보를 Intent에 담는다.
            putExtra("openNotifyFragment", "notificationFragment")
        }
        //Intent를 대기시킨다.
        //MainActivity를 flags의 설정을 한 인텐트에 대기시켜두고
        //특정 시점에 Intent를 수행하는 것이 보장되어 있다.
        //PendingIntent의 사용은
        //푸쉬알림, 바탕하면 런처, 위젯, AlarmManger를 통해 지정된 시간에 Intent를 할 수 있다.
        /**https://velog.io/@haero_kim/Android-PendingIntent-%EA%B0%9C%EB%85%90-%EC%9D%B5%ED%9E%88%EA%B8%B0*/

        val mainPendingIntent:PendingIntent = PendingIntent.getActivity(this,0,mainIntent,PendingIntent.FLAG_IMMUTABLE)
        //FlagImmutable은 intent 변경이 안되게

        val builder = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setContentTitle(messageTitle)
            .setContentText(messageContent)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)

        NotificationManagerCompat.from(this).apply {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "NOTIFYASDF ASD")
                notify(101, builder.build())
                return
            }
        }

    }
}
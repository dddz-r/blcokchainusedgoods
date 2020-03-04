package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

public class MyFirebaseMessangingService extends com.google.firebase.messaging.FirebaseMessagingService {

    //초기에 전화번호 인증 시 휴대폰 중복검사를 실행
    //중복이 아니면 휴대폰 번호와 인증번호 쌍을 생성 , 이 때 토큰도 같이 생성하여 서버로 보냄
    //여기서는 토큰을 만드는 메소드를 정의하고 해당 토큰을 auth 클래스에서 같이 보낸다.
    private static final String TAG = "FirebaseMegService";


    //토큰 생성
    @Override
    public void onNewToken(String token) {

        Log.d(TAG, "Refreshed token : " + token);
        //sendRegistrationToServer(token);
        //서버한테 토큰 보내는 기능인 듯
        //새로운 사용자 인증 시 해당 토큰 만들어서 저장하기

    }

    //메시지 전송
    //폰 번호 - 인증 번호 - 토큰 을 서버로 보내서 서버에서 remoteMessage를 보내 알람뜨게 해야할 듯...
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getNotification() != null) {

            String messageBody = remoteMessage.getNotification().getBody();
            String messageTitle = remoteMessage.getNotification().getTitle();

            Intent intent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Channel ID";
            Uri defualtSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.remove_dog)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defualtSoundUri)
                        .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                String channelName = "Channel Name";
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);

            }

            notificationManager.notify(0, notificationBuilder.build());

        }

    }

}

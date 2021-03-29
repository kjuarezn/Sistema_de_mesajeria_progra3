package com.example.mensajeriaprogra.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.mensajeriaprogra.R;
import com.example.mensajeriaprogra.channel.NotificationHelper;
import com.example.mensajeriaprogra.models.Message;
import com.example.mensajeriaprogra.receivers.ResponseReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingClient extends FirebaseMessagingService {

    public static final String NOTIFICATION_REPLY = "NotificationReply";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        String idNotification = data.get("idNotification");

        if (title != null) {

            if (title.equals("MENSAJE")) {
                getImageSender(data);
            }
            else {
                showNotification(title, body, idNotification);
            }
        }
    }

    private void showNotification(String title, String body, String idNotification) {
        NotificationHelper helper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = helper.getNotification(title, body);
        int id = Integer.parseInt(idNotification);
        Log.d("NOTIFICACION", "ID: " + id);
        helper.getManager().notify(id, builder.build());
    }

    private void getImageSender(final Map<String, String> data) {
        final String imageSender = data.get("imageSender");

        if (imageSender == null) {
            showNotificationMessage(data, null);
            return;
        }
        if (imageSender.equals("")) {
            showNotificationMessage(data, null);
            return;
        }

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(getApplicationContext())
                                .load(imageSender)
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        showNotificationMessage(data, bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                        showNotificationMessage(data, null);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                    }
                });
    }

    private void showNotificationMessage(Map<String, String> data, Bitmap bitmapReceiver) {
        String idNotification = data.get("idNotification");
        String usernameSender = data.get("usernameSender");
        String usernameReceiver = data.get("usernameReceiver");
        String messagesJSON = data.get("messagesJSON");
        String imageSender = data.get("imageSender");
        String imageReceiver = data.get("imageReceiver");
        String tokenSender = data.get("tokenSender");
        String tokenReceiver = data.get("tokenReceiver");
        int id = Integer.parseInt(idNotification);

        String idChat = data.get("idChat");
        String idSender = data.get("idSender");
        String idReceiver = data.get("idReceiver");

        Gson gson = new Gson();
        Message[] messages = gson.fromJson(messagesJSON, Message[].class);

        NotificationHelper helper = new NotificationHelper(getBaseContext());

        Intent intentResponse = new Intent(this, ResponseReceiver.class);
        intentResponse.putExtra("idNotification", id);
        intentResponse.putExtra("messages", messagesJSON);
        intentResponse.putExtra("usernameSender", usernameSender);
        intentResponse.putExtra("usernameReceiver", usernameReceiver);
        intentResponse.putExtra("imageSender", imageSender);
        intentResponse.putExtra("imageReceiver", imageReceiver);
        intentResponse.putExtra("idChat", idChat);
        intentResponse.putExtra("idSender", idSender);
        intentResponse.putExtra("idReceiver", idReceiver);
        intentResponse.putExtra("tokenSender", tokenSender);
        intentResponse.putExtra("tokenReceiver", tokenReceiver);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intentResponse, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_REPLY).setLabel("Tu mensaje...").build();

        NotificationCompat.Action actionResponse = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,
                "Responder",
                pendingIntent
        ).addRemoteInput(remoteInput)
                .build();

        NotificationCompat.Builder builder = helper.getNotificationMessage(messages, "", usernameSender, bitmapReceiver, null, actionResponse);
        helper.getManager().notify(id, builder.build());
    }

}
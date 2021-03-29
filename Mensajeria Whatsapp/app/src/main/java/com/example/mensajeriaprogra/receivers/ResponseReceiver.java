package com.example.mensajeriaprogra.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.mensajeriaprogra.R;
import com.example.mensajeriaprogra.channel.NotificationHelper;
import com.example.mensajeriaprogra.models.Message;

import com.example.mensajeriaprogra.providers.MessageProvider;
import com.example.mensajeriaprogra.providers.NotificationProvider;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.mensajeriaprogra.services.MyFirebaseMessagingClient.NOTIFICATION_REPLY;

public class ResponseReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        getMyImage(context, intent);
    }

    private void showNotification(Context context, Intent intent, Bitmap myBitmap) {
        String message = getMessageText(intent).toString();

        int id = intent.getExtras().getInt("idNotification");
        String messagesJSON = intent.getExtras().getString("messages");
        String usernameSender = intent.getExtras().getString("usernameSender");
        String usernameReceiver = intent.getExtras().getString("usernameReceiver");
        String imageSender = intent.getExtras().getString("imageSender");
        String imageReceiver = intent.getExtras().getString("imageReceiver");
        String idChat = intent.getExtras().getString("idChat");
        String idSender = intent.getExtras().getString("idSender");
        String idReceiver = intent.getExtras().getString("idReceiver");
        String tokenSender = intent.getExtras().getString("tokenSender");
        String tokenReceiver = intent.getExtras().getString("tokenReceiver");


        Gson gson = new Gson();
        Message[] messages = gson.fromJson(messagesJSON, Message[].class);

        NotificationHelper helper = new NotificationHelper(context);

        Intent intentResponse = new Intent(context, ResponseReceiver.class);
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

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intentResponse, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_REPLY).setLabel("Tu mensaje...").build();

        NotificationCompat.Action actionResponse = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,
                "Responder",
                pendingIntent
        ).addRemoteInput(remoteInput)
                .build();

        NotificationCompat.Builder builder = helper.getNotificationMessage(messages, message, usernameSender, null, myBitmap, actionResponse);
        helper.getManager().notify(id, builder.build());

        if (!message.equals("")) {
            final Message myMessage = new Message();
            myMessage.setIdChat(idChat);
            myMessage.setIdSender(idReceiver);
            myMessage.setIdReceiver(idSender);
            myMessage.setMessage(message);
            myMessage.setStatus("ENVIADO");
            myMessage.setType("texto");
            myMessage.setTimestamp(new Date().getTime());

            createMessage(myMessage);

            ArrayList<Message> messageArrayList = new ArrayList<>();
            messageArrayList.add(myMessage);
            sendNotification(
                    context,
                    messageArrayList,
                    String.valueOf(id),
                    usernameReceiver,
                    usernameSender,
                    imageReceiver,
                    imageSender,
                    idChat,
                    idSender,
                    idReceiver,
                    tokenSender,
                    tokenReceiver
            );
        }
    }

    private void createMessage(Message message) {
        MessageProvider messagesProvider = new MessageProvider();
        messagesProvider.create(message);
    }

    private void sendNotification(
            Context context,
            ArrayList<Message> messages,
            String idNotification,
            String usernameReceiver,
            String usernameSender,
            String imageReceiver,
            String imageSender,
            String idChat,
            String idSender,
            String idReceiver,
            String tokenSender,
            String tokenReceiver
    ) {
        Map<String, String> data = new HashMap<>();
        data.put("title", "MENSAJE");
        data.put("body", "texto mensaje");
        data.put("idNotification", idNotification);
        data.put("usernameReceiver", usernameSender);
        data.put("usernameSender", usernameReceiver);
        data.put("imageReceiver", imageSender);
        data.put("imageSender", imageReceiver);
        data.put("idChat", idChat);
        data.put("idSender", idReceiver);
        data.put("idReceiver", idSender);
        data.put("tokenSender", tokenReceiver);
        data.put("tokenReceiver", tokenSender);

        Gson gson = new Gson();
        String messagesJSON = gson.toJson(messages);
        data.put("messagesJSON", messagesJSON);
        NotificationProvider notificationProvider = new NotificationProvider();
        notificationProvider.send(context, tokenSender, data);
    }

    private void getMyImage(final Context context, final Intent intent) {
        final String myImage = intent.getExtras().getString("imageReceiver");

        if (myImage == null) {
            showNotification(context, intent, null);
            return;
        }
        if (myImage.equals("")) {
            showNotification(context, intent, null);
            return;
        }

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(context)
                                .load(myImage)
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        showNotification(context, intent, bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                        showNotification(context, intent, null);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                    }
                });
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(NOTIFICATION_REPLY);
        }
        return null;
    }

}
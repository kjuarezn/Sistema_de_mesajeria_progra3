package com.example.mensajeriaprogra.providers;


import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mensajeriaprogra.models.Message;
import com.example.mensajeriaprogra.utils.CompressorBitmapImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ImageProvider {

    StorageReference mStorage;
    FirebaseStorage mFirebaseStorage;
    int index=0;
    MessageProvider mMessageProvider;

    public ImageProvider(){
        mFirebaseStorage=FirebaseStorage.getInstance();
        mStorage =mFirebaseStorage.getReference();
        mMessageProvider = new MessageProvider();

    }
    public UploadTask save(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(),500,500);
        StorageReference storage= mStorage.child(new Date() +".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);
        return task;


    }
    public void uploadMultiple(final Context context, ArrayList<Message>messages) {

        Uri[] uri= new Uri[messages.size()];
        for (int i=0; i< messages.size();i++){
            File file = CompressorBitmapImage.reduceImageSize(new File(messages.get(i).getUrl()));

            uri[i]= Uri.parse("file://"+ file.getPath());

            final StorageReference ref = mStorage.child(uri[i].getLastPathSegment());
            ref.putFile(uri[i]).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()){
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url= uri.toString();
                                messages.get(index).setUrl(url);
                                mMessageProvider.create(messages.get(index));
                                index++;

                            }
                        });
                    }
                    else {
                        Toast.makeText(context, "Hubo un error al almacenar la imagen", Toast.LENGTH_SHORT).show();

                    }


                }
            });
        }

    }



    public Task<Uri> getDownloadUri(){
        return mStorage.getDownloadUrl();
    }

    public Task<Void> delete(String url){
        return mFirebaseStorage.getReferenceFromUrl(url).delete();

    }


}

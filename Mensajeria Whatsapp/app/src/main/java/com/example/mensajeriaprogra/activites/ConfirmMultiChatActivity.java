package com.example.mensajeriaprogra.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mensajeriaprogra.R;
import com.example.mensajeriaprogra.models.Chat;
import com.example.mensajeriaprogra.models.User;
import com.example.mensajeriaprogra.providers.AuthProvider;
import com.example.mensajeriaprogra.providers.ChatsProvider;
import com.example.mensajeriaprogra.providers.ImageProvider;
import com.example.mensajeriaprogra.providers.UsersProvider;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmMultiChatActivity extends AppCompatActivity {

    Chat mExtraChat;

    TextInputEditText mTextGroupName;
    Button mButtonConfirm;
    CircleImageView mCircleImagePhoto;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ImageProvider mImageProvider;
    ChatsProvider mChatsProvider;

    Options mOptions;
    ArrayList<String> mReturnsValues = new ArrayList<>();

    File mImageFile;
    String mGroupName = "";

    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_multi_chat);

        mTextGroupName = findViewById(R.id.textInputUsername);
        mButtonConfirm = findViewById(R.id.btnConfirm);

        mCircleImagePhoto = findViewById(R.id.circleImagePhoto);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mImageProvider = new ImageProvider();
        mChatsProvider =new ChatsProvider();

        mDialog = new ProgressDialog(ConfirmMultiChatActivity.this);
        mDialog.setTitle("Espere un momento");
        mDialog.setMessage("Guardando Información");



        String chat=getIntent().getStringExtra("chat");
        Gson gson= new Gson();
        mExtraChat = gson.fromJson(chat, Chat.class);



        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnsValues)                               //Pre selected Image Urls
                .setExcludeVideos(true)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(0)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");


        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroupName = mTextGroupName.getText().toString();
                if(!mGroupName.equals("") && mImageFile !=null){

                    saveImage();

                }
                else{

                    Toast.makeText(ConfirmMultiChatActivity.this, "Debe de seleccionar la imagen e ingrese su nombre de Usuario", Toast.LENGTH_LONG).show();
                }


            }
        });

        mCircleImagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starPix();

            }
        });


    }

    private void starPix() {
        Pix.start(ConfirmMultiChatActivity.this, mOptions);
    }

    

    private void goToHomeActivity() {
        mDialog.dismiss();
        Toast.makeText(ConfirmMultiChatActivity.this, "La informacion se actualizo correctamente", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(ConfirmMultiChatActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void saveImage() {
        mDialog.show();
        mImageProvider.save(ConfirmMultiChatActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            mExtraChat.setGroupName(mGroupName);
                            mExtraChat.setGroupImage(url);
                            createChat();

                        }
                    });

                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(ConfirmMultiChatActivity.this, "No se pudo almacenar la imagen", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    private void createChat() {
        mChatsProvider.create(mExtraChat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                goToHomeActivity();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnsValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            mImageFile= new File(mReturnsValues.get(0));
            mCircleImagePhoto.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(ConfirmMultiChatActivity.this, mOptions);
                } else {
                    Toast.makeText(ConfirmMultiChatActivity.this, "Activa los persmisos para Acceder a la camara", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}


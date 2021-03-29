package com.example.mensajeriaprogra.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;

import com.example.mensajeriaprogra.R;
import com.example.mensajeriaprogra.models.User;
import com.example.mensajeriaprogra.providers.AuthProvider;
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

import java.util.ArrayList;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;



public class CompleteInfoActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    Button mButtonConfirm;
    CircleImageView mCircleImagePhoto;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ImageProvider mImageProvider;

    Options mOptions;
    ArrayList<String> mReturnsValues = new ArrayList<>();

    File mImageFile;
    String mUsername = "";

    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);

        mTextInputUsername = findViewById(R.id.textInputUsername);
        mButtonConfirm = findViewById(R.id.btnConfirm);

        mCircleImagePhoto = findViewById(R.id.circleImagePhoto);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mImageProvider = new ImageProvider();
        mDialog = new ProgressDialog(CompleteInfoActivity.this);
        mDialog.setTitle("Espere un momento");
        mDialog.setMessage("Guardando Información");



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
                mUsername = mTextInputUsername.getText().toString();
                if(!mUsername.equals("") && mImageFile !=null){

                    saveImage();

                }
                else{

                    Toast.makeText(CompleteInfoActivity.this, "Debe de seleccionar la imagen e ingrese su nombre de Usuario", Toast.LENGTH_LONG).show();
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
        Pix.start(CompleteInfoActivity.this, mOptions);
    }

    private void updateUserInfo(String url) {

            User user = new User();
            user.setUsername(mUsername);
            user.setId(mAuthProvider.getId());
            user.setImage(url);
            mUsersProvider.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    goToHomeActivity();

                }
            });
    }

    private void goToHomeActivity() {
        mDialog.dismiss();
        Toast.makeText(CompleteInfoActivity.this, "La informacion se actualizo correctamente", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(CompleteInfoActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void saveImage() {
        mDialog.show();
        mImageProvider.save(CompleteInfoActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            updateUserInfo(url);

                        }
                    });

                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(CompleteInfoActivity.this, "No se pudo almacenar la imagen", Toast.LENGTH_SHORT).show();


                }
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
                    Pix.start(CompleteInfoActivity.this, mOptions);
                } else {
                    Toast.makeText(CompleteInfoActivity.this, "Activa los persmisos para Acceder a la camara", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
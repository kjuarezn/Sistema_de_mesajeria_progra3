package com.example.mensajeriaprogra.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.mensajeriaprogra.R;
import com.example.mensajeriaprogra.adapters.OptionsPagerAdapter;
import com.example.mensajeriaprogra.models.Message;
import com.example.mensajeriaprogra.providers.AuthProvider;
import com.example.mensajeriaprogra.providers.ImageProvider;
import com.example.mensajeriaprogra.utils.ShadowTransformer;

import java.util.ArrayList;
import java.util.Date;

public class ConfirmImageSendActivity extends AppCompatActivity {

    ViewPager mViewPager;
    String mExtraIdChat;
    String mExtraIdReceiver;
    ArrayList<String> data;
    ArrayList<Message> messages = new ArrayList<>();

    AuthProvider mAuthProvider;
    ImageProvider mImageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image_send);
        setStatusBarColor();

        mViewPager = findViewById(R.id.viewPager);
        mAuthProvider = new AuthProvider();
        mImageProvider = new ImageProvider();

        data = getIntent().getStringArrayListExtra("data");
        mExtraIdChat = getIntent().getStringExtra("idChat");
        mExtraIdReceiver = getIntent().getStringExtra("idReceiver");

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                Message m = new Message();
                m.setIdChat(mExtraIdChat);
                m.setIdSender(mAuthProvider.getId());
                m.setIdReceiver(mExtraIdReceiver);
                m.setStatus("ENVIADO");
                m.setTimestamp(new Date().getTime());
                m.setType("imagen");
                m.setUrl(data.get(i));
                m.setMessage("\uD83D\uDCF7imagen");
                messages.add(m);
            }
        }

        OptionsPagerAdapter pagerAdapter = new OptionsPagerAdapter(
                getApplicationContext(),
                getSupportFragmentManager(),
                dpToPixels(2, this),
                data
        );
        ShadowTransformer transformer = new ShadowTransformer(mViewPager, pagerAdapter);
        transformer.enableScaling(true);

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(false, transformer);

    }
    public void send() {
       mImageProvider.uploadMultiple(ConfirmImageSendActivity.this, messages);
       finish();
    }

    public void setMessage(int position, String message) {
        messages.get(position).setMessage(message);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void setStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullBlack, this.getTheme()));
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullBlack));
        }
    }

}
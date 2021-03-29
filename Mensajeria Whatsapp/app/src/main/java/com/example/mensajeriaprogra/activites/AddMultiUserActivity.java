package com.example.mensajeriaprogra.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.mensajeriaprogra.R;
import com.example.mensajeriaprogra.adapters.ContactsAdapter;
import com.example.mensajeriaprogra.adapters.MultiUsersAdapter;
import com.example.mensajeriaprogra.models.Chat;
import com.example.mensajeriaprogra.models.User;
import com.example.mensajeriaprogra.providers.AuthProvider;
import com.example.mensajeriaprogra.providers.UsersProvider;
import com.example.mensajeriaprogra.utils.MyToolbar;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class AddMultiUserActivity extends AppCompatActivity {

    RecyclerView mRecyclerViewContacts;
    FloatingActionButton mFabCheck;

    MultiUsersAdapter mAdapter;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;

    ArrayList<User> mUsersSelected;

    Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_multi_user);

        MyToolbar.show(AddMultiUserActivity.this,"Añadir",true);

        mRecyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        mFabCheck=findViewById(R.id.fabCheck);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddMultiUserActivity.this);
        mRecyclerViewContacts.setLayoutManager(linearLayoutManager);

        mFabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUsersSelected !=null){
                    if(mUsersSelected.size()>=2){
                        createChat();

                    }
                    else {
                        Toast.makeText(AddMultiUserActivity.this, "Seleccione almenos dos usuarios", Toast.LENGTH_SHORT).show();
                    }



                }
                else {
                    Toast.makeText(AddMultiUserActivity.this, "Por favor seleccione los ususarios", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void createChat(){
        Random random=new Random();
        int n=random.nextInt(100000);

        Chat chat= new Chat();
        chat.setId(UUID.randomUUID().toString());
        chat.setTimestamp(new Date().getTime());
        chat.setNumberMessages(1);
        chat.setWriting("");
        chat.setIdNotification(n);
        chat.setMultiChat(true);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(mAuthProvider.getId());

        for(User u:mUsersSelected){
            ids.add(u.getId());

        }
        chat.setIds(ids);
        Gson gson= new Gson();
        String chatJSON =gson.toJson(chat);

        Intent intent=new Intent(AddMultiUserActivity.this, ConfirmMultiChatActivity.class);
        intent.putExtra("chat", chatJSON);
        startActivity(intent);



    }


    public void setUsers(ArrayList<User> users){
        if(mMenu !=null){
            mUsersSelected =users;

            if(users.size()>0){
                mMenu.findItem(R.id.itemCount).setTitle(Html.fromHtml("<font color='#ffffff'>"+users.size()+"</font>"));
            }
            else {
                mMenu.findItem(R.id.itemCount).setTitle("");

            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mUsersProvider.getAllUsersByName();
        FirestoreRecyclerOptions<User>options=new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        mAdapter = new MultiUsersAdapter(options, AddMultiUserActivity.this);
        mRecyclerViewContacts.setAdapter(mAdapter);
        mAdapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_user_menu,menu);
        mMenu = menu;

        return true;
    }
}
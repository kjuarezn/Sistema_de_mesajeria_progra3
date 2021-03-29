package com.example.mensajeriaprogra.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mensajeriaprogra.R;
import com.example.mensajeriaprogra.adapters.ChatsAdapter;
import com.example.mensajeriaprogra.adapters.ContactsAdapter;
import com.example.mensajeriaprogra.models.Chat;
import com.example.mensajeriaprogra.models.User;
import com.example.mensajeriaprogra.providers.AuthProvider;
import com.example.mensajeriaprogra.providers.ChatsProvider;
import com.example.mensajeriaprogra.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatsFragment extends Fragment {

    View mView;
    RecyclerView mRecyclerViewChats;

    ChatsAdapter mAdapter;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;



    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chats, container, false);
        mRecyclerViewChats = mView.findViewById(R.id.recyclerViewChats);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mChatsProvider = new ChatsProvider();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewChats.setLayoutManager(linearLayoutManager);

        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();


        Query query = mChatsProvider.getUserChats(mAuthProvider.getId());
        FirestoreRecyclerOptions<Chat> options=new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build();
        mAdapter = new ChatsAdapter(options, getContext());
        mRecyclerViewChats.setAdapter(mAdapter);
        mAdapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter.getListener()!=null){
            mAdapter.getListener().remove();
        }
        if (mAdapter.getListenerLastMessage() != null) {
            mAdapter.getListenerLastMessage().remove();
        }
    }
}
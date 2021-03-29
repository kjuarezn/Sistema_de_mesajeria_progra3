package com.example.mensajeriaprogra.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mensajeriaprogra.R;
import com.example.mensajeriaprogra.activites.AddMultiUserActivity;
import com.example.mensajeriaprogra.activites.ChatActivity;
import com.example.mensajeriaprogra.models.User;
import com.example.mensajeriaprogra.providers.AuthProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MultiUsersAdapter extends FirestoreRecyclerAdapter<User, MultiUsersAdapter.ViewHolder> {

    Context context;
    AuthProvider authProvider;
    ArrayList<User> users=new ArrayList<>();

    public MultiUsersAdapter(FirestoreRecyclerOptions options, Context context) {
        super(options);
        this.context = context;
        authProvider = new AuthProvider();


    }

    @Override
    protected void onBindViewHolder(@NonNull final MultiUsersAdapter.ViewHolder holder, int position, @NonNull final User user) {

        if (user.getId().equals(authProvider.getId())) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            param.height = 0;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            param.topMargin = 0;
            param.bottomMargin = 0;
            holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.textViewInfo.setText(user.getInfo());
        holder.textViewUsername.setText(user.getUsername());
        if (user.getImage() != null) {
            if (!user.getImage().equals("")) {
                Picasso.with(context).load(user.getImage()).into(holder.circleImageUser);
            }
            else {
                holder.circleImageUser.setImageResource(R.drawable.ic_person);
            }
        }
        else {
            holder.circleImageUser.setImageResource(R.drawable.ic_person);
        }

        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectUser(holder, user);
            }
        });
    }

    private void selectUser(ViewHolder holder, User user) {
        if(!user.isSelected()){
            user.setSelected(true);
            holder.imageViewSelected.setVisibility(View.VISIBLE);
            users.add(user);
        }
        else {
            user.setSelected(false);
            holder.imageViewSelected.setVisibility(View.GONE);
            users.remove(user);
        }

        ((AddMultiUserActivity)context).setUsers(users);
    }


    @NonNull
    @Override
    public MultiUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_multi_users, parent, false);
        return new MultiUsersAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUsername;
        TextView textViewInfo;
        CircleImageView circleImageUser;
        ImageView imageViewSelected;
        View myView;

        public ViewHolder(View view) {
            super(view);
            myView = view;
            textViewUsername = view.findViewById(R.id.textViewUsername);
            textViewInfo = view.findViewById(R.id.textViewInfo);
            circleImageUser = view.findViewById(R.id.circleImageUser);
            imageViewSelected = view.findViewById(R.id.imageViewSelected);

        }

    }
}



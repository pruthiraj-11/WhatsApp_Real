package com.app.whatsappreal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whatsappreal.Models.UsersModel;
import com.app.whatsappreal.R;
import com.app.whatsappreal.ui.Activity.ChatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private final ArrayList<UsersModel> lists;
    private final Context context;

    public ContactAdapter(ArrayList<UsersModel> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_contact_item,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.contactUserName.setText(lists.get(position).getUserName());
        holder.contactUserAbout.setText(lists.get(position).getBio());
        Glide.with(context).load(lists.get(position).getImageProfile()).placeholder(R.drawable.placeholder_profile).into(holder.contactImageProfile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId",lists.get(position).getUserId())
                        .putExtra("userName",lists.get(position).getUserName())
                        .putExtra("imageProfile",lists.get(position).getImageProfile()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactUserName;
        private final TextView contactUserAbout;
        private final CircleImageView contactImageProfile;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactUserName=itemView.findViewById(R.id.contactUserName);
            contactUserAbout=itemView.findViewById(R.id.contactUserAbout);
            contactImageProfile=itemView.findViewById(R.id.contactImageProfile);
        }
    }
}

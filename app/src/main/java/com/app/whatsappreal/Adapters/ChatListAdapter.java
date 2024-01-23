package com.app.whatsappreal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whatsappreal.Models.ChatList;
import com.app.whatsappreal.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    private ArrayList<ChatList> lists;
    private Context context;

    public ChatListAdapter(ArrayList<ChatList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_chat_list,parent,false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.usernamechat.setText(lists.get(position).getUserName());
        holder.chatlastmessage.setText(lists.get(position).getLastMessage());
        Glide.with(context).load(lists.get(position).getUserProfileURL()).placeholder(R.drawable.placeholder_profile).into(holder.userProfilePic);
        holder.lastmessageDateTime.setText(lists.get(position).getLastMessageDate());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {
        private final TextView usernamechat;
        private final TextView lastmessageDateTime;
        private final TextView chatlastmessage;
        private final CircleImageView userProfilePic;
        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            usernamechat=itemView.findViewById(R.id.usernamechat);
            lastmessageDateTime=itemView.findViewById(R.id.lastmessageDateTime);
            chatlastmessage=itemView.findViewById(R.id.chatlastmessage);
            userProfilePic=itemView.findViewById(R.id.profile_image);
        }
    }
}

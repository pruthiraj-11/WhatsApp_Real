package com.app.whatsappreal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whatsappreal.Models.CallList;
import com.app.whatsappreal.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.CallListViewHolder> {
    private ArrayList<CallList> lists;
    private Context context;

    public CallListAdapter(ArrayList<CallList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public CallListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_chat_list,parent,false);
        return new CallListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallListViewHolder holder, int position) {
        holder.usernamechat.setText(lists.get(position).getUserName());
        Glide.with(context).load(lists.get(position).getUserProfileURL()).placeholder(R.drawable.placeholder_profile).into(holder.userProfilePic);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class CallListViewHolder extends RecyclerView.ViewHolder {
        private final TextView usernamechat;
        private final TextView lastmessageDateTime;
        private final TextView chatlastmessage;
        private final CircleImageView userProfilePic;
        public CallListViewHolder(@NonNull View itemView) {
            super(itemView);
            usernamechat=itemView.findViewById(R.id.usernamechat);
            lastmessageDateTime=itemView.findViewById(R.id.lastmessageDateTime);
            chatlastmessage=itemView.findViewById(R.id.chatlastmessage);
            userProfilePic=itemView.findViewById(R.id.profile_image);
        }
    }
}

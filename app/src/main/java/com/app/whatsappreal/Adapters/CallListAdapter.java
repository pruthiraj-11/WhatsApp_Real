package com.app.whatsappreal.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whatsappreal.Models.CallList;
import com.app.whatsappreal.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.CallListViewHolder> {
    private final ArrayList<CallList> lists;
    private final Context context;

    public CallListAdapter(ArrayList<CallList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public CallListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_call_list,parent,false);
        return new CallListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallListViewHolder holder, int position) {
        holder.usernamecall.setText(lists.get(position).getUserName());
        holder.calldatetime.setText(lists.get(position).getDate());
        Glide.with(context).load(lists.get(position).getUserProfileURL()).placeholder(R.drawable.placeholder_profile).into(holder.userProfilePic);

        if (lists.get(position).getCallType().equals("missed")) {
            holder.callType.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.arrow_downward_24px));
        } else if (lists.get(position).getCallType().equals("incoming")){
            holder.callType.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.arrow_downward_24px));
            holder.callType.getDrawable().setTint(Color.parseColor("#ff669900"));
        } else {
            holder.callType.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.arrow_upward_24px));
            holder.callType.getDrawable().setTint(Color.parseColor("#ff669900"));
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class CallListViewHolder extends RecyclerView.ViewHolder {
        private final TextView usernamecall;
        private final TextView calldatetime;
        private final CircleImageView userProfilePic;
        private final ImageView callType;
        public CallListViewHolder(@NonNull View itemView) {
            super(itemView);
            usernamecall=itemView.findViewById(R.id.usernamecall);
            calldatetime=itemView.findViewById(R.id.calldatetime);
            userProfilePic=itemView.findViewById(R.id.profile_image_call);
            callType=itemView.findViewById(R.id.callType);
        }
    }
}

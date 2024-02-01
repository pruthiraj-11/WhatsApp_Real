package com.app.whatsappreal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whatsappreal.Models.ChatsMessageModel;
import com.app.whatsappreal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class ChatsMessageAdapter extends RecyclerView.Adapter<ChatsMessageAdapter.ChatsMessageViewHolder> {
    private final ArrayList<ChatsMessageModel> lists;
    private final Context context;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private FirebaseUser firebaseUser;

    public ChatsMessageAdapter(ArrayList<ChatsMessageModel> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatsMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (viewType==MSG_TYPE_LEFT) {
            view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
        } else if (viewType==MSG_TYPE_RIGHT) {
            view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
        }
        return new ChatsMessageViewHolder(Objects.requireNonNull(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsMessageViewHolder holder, int position) {
        holder.bind(lists.get(position));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (lists.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public static class ChatsMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView senderReceiverNewMessage;
        public ChatsMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderReceiverNewMessage=itemView.findViewById(R.id.senderReceiverNewMessage);
        }
        private void bind(ChatsMessageModel chatsMessageModel) {
            senderReceiverNewMessage.setText(chatsMessageModel.getTextMessage());
        }
    }
}
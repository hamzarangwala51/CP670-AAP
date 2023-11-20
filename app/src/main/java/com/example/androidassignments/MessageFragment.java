package com.example.androidassignments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MessageFragment extends Fragment {
    int position = 0;
    ChatWindow chatWindow;

    public MessageFragment(ChatWindow cw) {
        chatWindow = cw;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_details, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TextView txtMsg = view.findViewById(R.id.messageTextView);
        TextView txtMsgId = view.findViewById(R.id.idTextView);
        Button deleteMsg = view.findViewById(R.id.deleteButton);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            long msgId = bundle.getLong("msg_id", 0);
            String msg = bundle.getString("msg", "");
            position = bundle.getInt("position", 0);
            txtMsg.setText(msg);
            txtMsgId.setText(String.valueOf(msgId));
        }
        deleteMsg.setOnClickListener(v -> {
            if (chatWindow == null) {
                Intent intent = new Intent();
                long msgId = Long.parseLong(txtMsgId.getText().toString());
                intent.putExtra("msg_id", msgId);
                intent.putExtra("position", position);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            } else {
                long msgId = Long.parseLong(txtMsgId.getText().toString());
                chatWindow.deleteMessage(msgId, position);
            }
        });
    }
}

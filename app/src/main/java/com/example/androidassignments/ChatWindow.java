package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatWindow extends AppCompatActivity {
    public final String activityName = "ChatWindow";
    ListView chatList;
    Button sendButton;
    EditText chatEdit;
    ArrayList<String> chatMessages;
    FrameLayout displayMessageDetails;
    Cursor cursor;
    public final int REQUEST_FOR_ACTIVITY = 13;
    ChatAdapter messageAdapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatList = findViewById(R.id.chatListView);
        sendButton = findViewById(R.id.sendButton);
        chatEdit = findViewById(R.id.chatEditText);
        chatMessages = new ArrayList<>();
        displayMessageDetails = findViewById(R.id.frame_layout);
        Boolean frameExists = displayMessageDetails == null ? false : true;

        ChatDatabaseHelper dbHelper = ChatDatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String MESSAGES_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        ChatDatabaseHelper.TABLE_NAME);
        cursor = db.rawQuery(MESSAGES_SELECT_QUERY, null);
        try {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                    Log.i(activityName,"SQL Message : " + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                    chatMessages.add(message);
                } while(cursor.moveToNext());
            }

            Log.i(activityName, "Cursors column count = " + cursor.getColumnCount());

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.i(activityName, "Column " + (i + 1) + " name = " + cursor.getColumnName(i));
            }
        } catch (Exception e) {
            Log.d("DB", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        messageAdapter = new ChatAdapter(this, chatMessages);
        chatList.setAdapter(messageAdapter);

        sendButton.setOnClickListener(view -> {
            String message = chatEdit.getText().toString().trim();
            if (validateText(message)) {
                chatMessages.add(message);
                chatEdit.setText("");
                messageAdapter.notifyDataSetChanged();

                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                    db.insertOrThrow(ChatDatabaseHelper.TABLE_NAME, null, values);
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.d(activityName, "Error while trying to add message to database");
                } finally {
                    db.endTransaction();
                }
            }
        });

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (displayMessageDetails!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("msg_id", id);
                    bundle.putString("msg", chatMessages.get(position));
                    bundle.putInt("position", position);
                    MessageFragment ms = new MessageFragment(ChatWindow.this);
                    ms.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.frame_layout, ms)
                            .commit();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("msg_id", id);
                    bundle.putString("msg", chatMessages.get(position));
                    bundle.putInt("position", position);
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_FOR_ACTIVITY);
                }
            }
        });
    }

    public void deleteMessage(long msgId, int position) {
        try {
            ChatDatabaseHelper dbHelper = ChatDatabaseHelper.getInstance(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "= ?",  new String[] { String.valueOf(msgId) });
            messageAdapter.remove(messageAdapter.getItem(position));
            messageAdapter.notifyDataSetChanged();
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_FOR_ACTIVITY) {
            try {
                long msgId = data.getLongExtra("msg_id", 0);
                int position = data.getIntExtra("position", 0);
                ChatDatabaseHelper dbHelper = ChatDatabaseHelper.getInstance(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "= ?",  new String[] { String.valueOf(msgId) });
                messageAdapter.remove(messageAdapter.getItem(position));
                messageAdapter.notifyDataSetChanged();
            } catch (Exception e) {

            }
        }
    }

    public static boolean validateText(String message) {
        return message.isEmpty() ? false : true;
    }

    class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public ChatAdapter(Context ctx, ArrayList<String> messages) {
            super(ctx, 0, messages);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            ChatDatabaseHelper dbHelper = ChatDatabaseHelper.getInstance(getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String MESSAGES_SELECT_QUERY =
                    String.format("SELECT * FROM %s",
                            ChatDatabaseHelper.TABLE_NAME);
            cursor = db.rawQuery(MESSAGES_SELECT_QUERY, null);
            cursor.moveToPosition(position);
            int msgId = cursor.getInt(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_ID));
            return msgId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));

            return result;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatDatabaseHelper dbHelper = ChatDatabaseHelper.getInstance(this);
        dbHelper.close();
    }
}
package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private ArrayList<String> MessageStore = new ArrayList<>();
    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private static final String ACTIVITY_NAME = "ChatWindow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        setTitle(getString(R.string.ChatActivity_name));
        ListView chatListView = findViewById(R.id.chatListView);
        EditText chatEditText = findViewById(R.id.chatEditText);
        Button sendButton = findViewById(R.id.sendButton);
        ChatAdapter messageAdapter =new ChatAdapter( this );
        chatListView.setAdapter(messageAdapter);
        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Query for existing chat messages
        String[] projection = {
                ChatDatabaseHelper.KEY_MESSAGE
        };
        Cursor cursor = db.query(
                ChatDatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String message = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
                MessageStore.add(message);
                cursor.moveToNext();
            }

            Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);
                Log.i(ACTIVITY_NAME, "Column Name " + i + ": " + columnName);
            }

            cursor.close();
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                    String message = chatEditText.getText().toString().trim();

                // Check if the message is not empty
                if (validateMessage(message)) {
                    // Add the message to the ArrayList
                    MessageStore.add(message);
                    messageAdapter.notifyDataSetChanged();
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                    db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
                    // Clear the EditText field
                    chatEditText.setText("");


                    // You can update the ListView or perform any other necessary actions here
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the database when the activity is destroyed
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public static boolean validateMessage(String message){
        if(!message.isEmpty()){
            return  true;
        }else{
            return  false;
        }
    }
    public class ChatAdapter extends ArrayAdapter<String> {

        // Constructor for ChatAdapter
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            // Return the number of chat messages in the list
            return MessageStore.size();
        }

        @Override
        public String getItem(int position) {
            // Return the chat message at the specified position
            return MessageStore.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the LayoutInflater
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            // Inflate chat_row_incoming or chat_row_outgoing based on the position
            View result;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            // Get the TextView that holds the message
            TextView message = result.findViewById(R.id.message_text);

            // Set the text in the TextView
            message.setText(getItem(position));


            return result;
        }


    }
}
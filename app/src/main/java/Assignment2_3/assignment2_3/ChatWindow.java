package Assignment2_3.assignment2_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidassignments.R;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private ArrayList<String> MessageStore = new ArrayList<>();
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
                    // Clear the EditText field
                    chatEditText.setText("");


                    // You can update the ListView or perform any other necessary actions here
                }
            }
        });
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
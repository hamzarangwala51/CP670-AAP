package com.example.androidassignments;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        Bundle bundle = getIntent().getExtras();
        MessageFragment ms = new MessageFragment(null);
        ms.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.container, ms)
                .commit();

    }
}

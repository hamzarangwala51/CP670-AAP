package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String Resume,StartMsg,Pause,Stop,Destroy,onSaveInstanceState,onRestoreInstanceState;
    String ACTIVITY_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.MainActivity_name));
        Button btn = findViewById(R.id.main_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ListItemsActivity using startActivityForResult
                Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 10);
            }
        });
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if(requestCode==10&& responseCode ==Activity.RESULT_OK){
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
        }
    }
    public void chtbtnFunc(View view){
        Log.i(ACTIVITY_NAME,"User clicked Start Chat");
        Intent intent = new Intent(this, ChatWindow.class);
        startActivity(intent);

    }
    protected void onResume() {
        super.onResume();

        Log.i(Resume,"Resume Message from Main");

    }
    protected void onStart(){
        super.onStart();
        Log.i(StartMsg,"OnStart Message from Main");
    }
    protected void onPause(){
        super.onPause();
        Log.i(Pause,"OnPause Message from Main");
    }
    protected void onStop(){
        super.onStop();
        Log.i(Stop,"onStop Message from Main");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.i(Destroy,"On DestroyMessage");
    }
    protected void onSaveInstanceState (Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(onSaveInstanceState,"onSaveInstanceState from Main");
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(onRestoreInstanceState,"onRestoreInstanceState");
    }

}

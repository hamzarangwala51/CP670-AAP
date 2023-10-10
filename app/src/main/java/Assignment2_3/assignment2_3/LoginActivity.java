package Assignment2_3.assignment2_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidassignments.R;

public class LoginActivity extends AppCompatActivity {
    private EditText emailid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
             EditText Loginfld = findViewById(R.id.emailfld);
             SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
              String s1 = sh.getString("emailid", "");
              Loginfld.setText(s1);
//        String s1 = sh.getString("name", "");

        //Button loginbt = findViewById(R.id.button3);

    }
    public void loginsave(View view){
        EditText emailaddr = findViewById(R.id.emailfld);
        EditText passwrdtext = findViewById(R.id.psswdtxt);
        String emailStr = emailaddr.getText().toString().trim();
        if(emailStr!=null&& passwrdtext.getText().toString().length()>8){
            if(emailStr.length()>=1){
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (emailStr.matches(emailPattern))
                {
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putString("emailid",emailaddr.getText().toString());
                    myEdit.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                   // Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"Invalid email address", Toast.LENGTH_SHORT).show();
                }
        }else
        {
            Toast.makeText(this,"Please Enter Valid email address or Password", Toast.LENGTH_SHORT).show();
        }

        }
    protected void onResume() {
        super.onResume();
    }
    protected void onStart(){
        super.onStart();
    }
    protected void onPause(){
        super.onPause();
    }
    protected void onStop(){
        super.onStop();
    }
    protected void onDestroy(){
        super.onDestroy();
    }
}

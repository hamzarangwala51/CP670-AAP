package com.example.androidassignments;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.androidassignments.databinding.ActivityTestToolbarBinding;


public class TestToolbar extends AppCompatActivity {

    private ActivityTestToolbarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_graph);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You selected item 1", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });


    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the toolbar menu from the XML file
        getMenuInflater().inflate(R.menu.toolbar_menu1, menu);
        return true;
    }
String messageForSnackBar;
    String getTextMessage(){
        if(messageForSnackBar!=null){
            return messageForSnackBar;
        }else{
            return"No message";
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_one) {
            View view = findViewById(R.id.fab);

            Snackbar.make(view,getTextMessage(), Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_two) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you want to go back?");
// Add the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    finish();
//                    Intent intent = new Intent(this, MainActivity.class);
//                    startActivity(intent);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        } else if (id == R.id.action_three) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog, null);
            builder.setView(dialogView);
            builder.setTitle("SnackBar Message:");
// Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    EditText message = dialogView.findViewById(R.id.editTextNewMessage);
                    messageForSnackBar = message.getText().toString();
                    //getTextMessage(newMessage);
                    // User clicked OK button
                    //finish();
//                    Intent intent = new Intent(this, MainActivity.class);
//                    startActivity(intent);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
// Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            Intent intent1 = new Intent(this, ChatWindow.class);
//            startActivity(intent1);
            //return true;
        } else if (id == R.id.action_about) {
            Toast.makeText(this, "Version 1.0 , by HAMZA", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            // Start an activity...
            // Replace this comment with your code to start the activity
            //return true;
            // Replace this comment with your code to start the activity
            //  return true; // Handle this option
            return super.onOptionsItemSelected(item);
        }
    }
}
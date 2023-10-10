package Assignment2_3.assignment2_3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.androidassignments.R;

public class ListItemsActivity extends AppCompatActivity {
    int REQUEST_IMAGE_CAPTURE;
    String Resume,StartMsg,Pause,Stop,Destroy,onSaveInstanceState,onRestoreInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        setTitle(getString(R.string.ListitemActivity_name));
    Switch mySwitch = findViewById(R.id.switch1);
    mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String message = isChecked ? "Switch is On" : "Switch is Off";
            int duration = isChecked ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), message, duration);
            toast.show();
        }
    });
        CheckBox checkboxFinishActivity = findViewById(R.id.checkBox);

        // Set an OnCheckedChangeListener for the checkbox
        checkboxFinishActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Show a dialog when the checkbox state changes
                if (isChecked) {
                    showFinishConfirmationDialog();
                }
            }
        });


    }
    private void showFinishConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button, finish the activity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "Here is my response");
                        setResult(Activity.RESULT_OK, resultIntent);
                        Toast toast = Toast.makeText(getApplicationContext(), "You logged Out",Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        // You can handle this case if needed
                    }
                })
                .show();
    }
    public static void print(Context context, String message) {
        // Use Toast to display the message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        // Alternatively, you can use Snackbar
        // Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
    public void imageClicked(View imageView) {

        ImageButton btnImg = findViewById(R.id.imageButton);
        print(this, "Camera Opened");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            return;
        }else {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        //}
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageButton btnImg = findViewById(R.id.imageButton);
            btnImg.setImageBitmap(image);
            // do whatever you want with the image now
        }
    }

    protected void onResume() {
        super.onResume();
        Log.i(Resume,"Resume Message from Listitems");

    }
    protected void onStart(){
        super.onStart();
        Log.i(StartMsg,"Resume Message from Listitems");
    }
    protected void onPause(){
        super.onPause();
        Log.i(Pause,"Resume Message from Listitems");
    }
    protected void onStop(){
        super.onStop();
        Log.i(Stop,"Resume Message from Listitems");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.i(Destroy,"Resume Message from Listitems");
    }
}
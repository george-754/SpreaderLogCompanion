package com.grhprogramming.spreaderlogcompanion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    private Button btnSave;
    private EditText editKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Add Back button to the navigation bar to go back to the main screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.user_profile);

        // Element variables
        btnSave = findViewById(R.id.btnSaveApi);
        editKey = findViewById(R.id.api_edit);

        // Initialize tinydb used for prefs
        TinyDB tinyDB = new TinyDB(getApplicationContext());

        // Load user key into edit text box when page is loaded
        editKey.setText(tinyDB.getString("user_key"));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save key to user prefs
                tinyDB.putString("user_key", editKey.getText().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Api key saved.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

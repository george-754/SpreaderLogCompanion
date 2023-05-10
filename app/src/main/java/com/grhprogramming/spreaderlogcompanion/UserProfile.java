package com.grhprogramming.spreaderlogcompanion;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class UserProfile extends AppCompatActivity {

    private Button btnSave;
    private TextInputLayout editKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Actionbar color and title and Add Back button to the navigation bar to go back to the main screen
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#800000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.user_profile);

        // Element variables
        btnSave = findViewById(R.id.btnSaveApi);
        editKey = findViewById(R.id.api_edit);

        // Initialize tinydb used for prefs
        TinyDB tinyDB = new TinyDB(getApplicationContext());

        // Load user key into edit text box when page is loaded
        editKey.getEditText().setText(tinyDB.getString("user_key"));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save key to user prefs
                tinyDB.putString("user_key", editKey.getEditText().getText().toString());

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

package com.grhprogramming.spreaderlogcompanion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;


public class AddJob extends AppCompatActivity {

    private EditText txtDate;
    private AutoCompleteTextView txtFarmer;
    private EditText txtField;
    private AutoCompleteTextView txtProduct;
    private EditText txtAcres;

    private DbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button btnAdd;
        // Add back button to the navigation bar to go back to the main screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_job);

        txtDate = findViewById(R.id.date_text);
        txtFarmer = findViewById(R.id.farmer_text);
        txtField = findViewById(R.id.field_text);
        txtProduct = findViewById(R.id.product_text);
        txtAcres = findViewById(R.id.acres_text);

        btnAdd = findViewById(R.id.add_button);

        db = new DbHandler(this);

        // Add date picker to the date edit box
        txtDate.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddJob.this, (datePicker, year1, monthofyear, dayofmonth) -> txtDate.setText((monthofyear + 1) + "/" + dayofmonth + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        TinyDB tinyDB = new TinyDB(getApplicationContext());
        // Product edit text box suggestions
        // String[] products = getResources().getStringArray(R.array.list_of_products);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, products);

        // Array list for suggestions on the farmer text box
        ArrayList<String> farmers = tinyDB.getListString("groupedFarmers");
        ArrayAdapter<String> f_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, farmers);
        txtFarmer.setAdapter(f_adapter);

        // Array list for suggestions on the products text box
        ArrayList<String> products = tinyDB.getListString("groupedProducts");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, products);
        txtProduct.setAdapter(adapter);

        btnAdd.setOnClickListener(view -> {
            // ADD IF STATEMENTS FOR ERROR CHECKS ON THE TEXT ENTERED ON THE ADD FORM
            Integer error_check = 0; // Variable to hold if error occurred during error check

            final String date = txtDate.getText().toString();
            final String farmer = txtFarmer.getText().toString();
            final String field = txtField.getText().toString();
            final String product = txtProduct.getText().toString();
            final String acres = txtAcres.getText().toString();

            if (date.isEmpty()) {
                error_check = 1;
            } else if (farmer.isEmpty()) {
                error_check = 1;
            } else if (field.isEmpty()) {
                error_check = 1;
            } else if (product.isEmpty()) {
                error_check = 1;
            } else if (acres.isEmpty()) {
                error_check = 1;
            }

            if (error_check == 0) {
                db.insertJob(date, farmer, field, product, acres);
                // When add job button is clicked reload the main page
                Intent main = new Intent(AddJob.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
            }
            else {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error adding job.", Toast.LENGTH_SHORT).show());
            }

        });
    }
}

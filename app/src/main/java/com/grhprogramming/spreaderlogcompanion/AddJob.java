package com.grhprogramming.spreaderlogcompanion;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;


public class AddJob extends AppCompatActivity {

    private TextInputLayout txtDate;
    private TextInputLayout txtFarmer;
    private TextInputLayout txtField;
    private TextInputLayout txtProduct;
    private TextInputLayout txtAcres;
    private MaterialDatePicker dPicker;

    private DbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button btnAdd;

        // Add back button to the navigation bar to go back to the main screen and set color and title
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#800000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_job);

        txtDate = findViewById(R.id.date_text);
        txtFarmer = findViewById(R.id.farmer_text);
        txtField = findViewById(R.id.field_text);
        txtProduct = findViewById(R.id.product_text);
        txtAcres = findViewById(R.id.acres_text);

        btnAdd = findViewById(R.id.add_button);

        // Month defines. 0 = 1 for January and so forth
        String[] c_month = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        // Initialize the material design date picker object
        dPicker = new MaterialDatePicker();

        // Build the Material design date picker with a title and have the current date selected by default
        dPicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        db = new DbHandler(this);

        // Add date picker to the date edit box
        /*txtDate.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddJob.this, (datePicker, year1, monthofyear, dayofmonth) -> txtDate.getEditText().setText((monthofyear + 1) + "/" + dayofmonth + "/" + year1), year, month, day);
            datePickerDialog.show();
        });*/
        txtDate.setEndIconOnClickListener(v -> dPicker.show(getSupportFragmentManager(), "tag"));

        dPicker.addOnPositiveButtonClickListener(nv -> {
            // Get the current date time
            final Calendar c = Calendar.getInstance();

            // Set the c calendar date to the selected date pickers date
            c.setTimeInMillis(Long.parseLong(dPicker.getSelection().toString()));

            // Add 1 day to the date
            c.add(Calendar.DAY_OF_MONTH, 1);

            // String to be displayed in the date text box
            String a_date = String.format("%s/%s/%s", c_month[c.get(Calendar.MONTH)], c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));

            // Set the text box with the currently selected date from the date pickers selection
            // txtDate.getEditText().setText((c.get(Calendar.MONTH)) + "/" + (c.get(Calendar.DAY_OF_MONTH)) + "/" + c.get(Calendar.YEAR));
            txtDate.getEditText().setText(a_date);
        });

        TinyDB tinyDB = new TinyDB(getApplicationContext());
        // Product edit text box suggestions
        // String[] products = getResources().getStringArray(R.array.list_of_products);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, products);

        // Array list for suggestions on the farmer text box
        ArrayList<String> farmers = tinyDB.getListString("groupedFarmers");
        ArrayAdapter<String> f_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, farmers);
        ((AutoCompleteTextView) txtFarmer.getEditText()).setAdapter(f_adapter);


        // Array list for suggestions on the products text box
        ArrayList<String> products = tinyDB.getListString("groupedProducts");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, products);
        //txtProduct.setAdapter(adapter);
        ((AutoCompleteTextView) txtProduct.getEditText()).setAdapter(adapter);

        btnAdd.setOnClickListener(view -> {
            // ADD IF STATEMENTS FOR ERROR CHECKS ON THE TEXT ENTERED ON THE ADD FORM
            Integer error_check = 0; // Variable to hold if error occurred during error check

            final String date = txtDate.getEditText().getText().toString();
            final String farmer = txtFarmer.getEditText().getText().toString();
            final String field = txtField.getEditText().getText().toString();
            final String product = txtProduct.getEditText().getText().toString();
            final String acres = txtAcres.getEditText().getText().toString();

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

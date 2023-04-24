package com.grhprogramming.spreaderlogcompanion;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SyncData extends AppCompatActivity {

    private TextView txtConnection;
    private Button btnSync;
    private Button btnUpdate;
    private String connection;
    private TextView txtCount;
    private DbHandler db;
    private Integer dataIndex;
    ArrayList<DataModel> dataList;

    private TinyDB tinyDB;
    // private ProgressBar progressBar;
    // private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        // Add Back button to the navigation bar to go back to the main screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.sync_job);

        // Initialize activity elements
        txtConnection = findViewById(R.id.text_connection);
        btnSync = findViewById(R.id.button_sync);
        btnUpdate = findViewById(R.id.button_update);
        txtCount = findViewById(R.id.text_count_label);
        // progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);

        // Use tiny db to store and retrieve prefs
        tinyDB = new TinyDB(getApplicationContext());

        // Display the query count
        db = new DbHandler(this);
        String que = "" + db.getQueryCount();
        txtCount.setText(que);

        // Disable sync button if there is no jobs in the que
        if (db.getQueryCount() > 0) {
            btnSync.setEnabled(true);
        }
        else {
            btnSync.setEnabled(false);
        }

        // Create a client
        OkHttpClient okHttpClient = new OkHttpClient();

        // Build a request
        Request request = new Request.Builder()
                .url("https://georgehopkins25.pythonanywhere.com/companion")
                .build();

        // Make call asynchronously
        // Get Request to see if we can connect to the server
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SyncData.this, "Server Down", Toast.LENGTH_SHORT).show();
                        txtConnection.setText(R.string.connect_error);
                        connection = "no";
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            txtConnection.setText(response.body().string());
                            connection = "yes";
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        // txtResponseView.setText("Connected!");
                    }
                });

                // txtResponseView.setText("Connected!");
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display progress bar
                // progressBar.setVisibility(View.VISIBLE);
                // Display loading progress bar
//                progressDialog = new ProgressDialog(SyncData.this);
//                progressDialog.setMessage("Uploading data to server...");
//                progressDialog.setTitle("Sync Data");
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progressDialog.show();
//                progressDialog.setCancelable(false);

                // Add code to sync data to the spreader log web app
                dataList = new ArrayList<DataModel>();
                dataList = db.getAll();

                OkHttpClient okHttpClient1 = new OkHttpClient();
                // DataModel dataModel;

                // Retrieve api key from user preferences
                String userKey = tinyDB.getString("user_key");

                for (int x = 0; x < dataList.size(); x++) {
                    /*Log.i("TESTING", "Query Count: " + db.getQueryCount());
                    Log.i("TESTING", "Loop");
                    // dataModel = db.getFirst();
                    Log.i("TESTING", "Name: " + dataList.get(x).getFarmer());
                    Log.i("TESTING", "Date: " + dataList.get(x).getDate());
                    Log.i("TESTING", "Field: " + dataList.get(x).getField());
                    Log.i("TESTING", "Product: " + dataList.get(x).getProduct());
                    Log.i("TESTING", "Acres: " + dataList.get(x).getAcres());
                    dataIndex = Integer.parseInt(dataList.get(x).getId());
                    Log.i("TESTING", "Data Index: " + dataIndex);*/

                    // Get the id of the data record and store it to be used later to delete the record
                    dataIndex = Integer.parseInt(dataList.get(x).getId());

                    RequestBody formbody
                            = new FormBody.Builder()
                            .add("rUser", userKey)
                            .add("rDate", dataList.get(x).getDate())
                            .add("rFarmer", dataList.get(x).getFarmer())
                            .add("rField", dataList.get(x).getField())
                            .add("rProduct", dataList.get(x).getProduct())
                            .add("rAcres", dataList.get(x).getAcres())
                            .build();

                    Request request1 = new Request.Builder().url("https://georgehopkins25.pythonanywhere.com/companion")
                            .post(formbody)
                            .build();

                    okHttpClient1.newCall(request1).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            // progressDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Unable to connect to the server.", Toast.LENGTH_SHORT).show();
                                    Log.i("TESTING", "Failed to connect!");
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            // progressDialog.dismiss();
                            if (response.body().string().equals("received")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Data received.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // Delete job on successful request
                                db.DeleteJob(dataIndex);
                                Log.i("TESTING", "Deleted Job!");

                            } else {
                                call.cancel();
                                Log.i("TESTING", "Companion Error!");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Invalid api key.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    // Sleep
                    try {
                        Thread.sleep(2000);
                        Log.i("TESTING", "Sleep!");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // progressBar.setVisibility(View.GONE);
                // Reload sync data view
                finish();
                startActivity(getIntent());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> farmers_list = new ArrayList<>();
                ArrayList<String> products_list = new ArrayList<>();

                OkHttpClient client = new OkHttpClient();

                Request request1 = new Request.Builder()
                        .url("https://georgehopkins25.pythonanywhere.com/companion/sync")
                        .build();

                client.newCall(request1).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            try {
                                JSONObject obj = new JSONObject(response.body().string());
                                // Get farmers data
                                JSONArray farmersArray = obj.getJSONArray("grouped_farmers");
                                // Get products data
                                JSONArray productsArray = obj.getJSONArray("grouped_products");

                                // Loop through jsonarray farmers and add to the farmers list
                                for (int i = 0; i < farmersArray.length(); i++) {
                                    farmers_list.add(farmersArray.getString(i));
                                }

                                // Loop though the jsonarray products and add to the products list
                                for (int x = 0; x < productsArray.length(); x++) {
                                    products_list.add(productsArray.getString(x));
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // Store farmers list and products lists in prefs for suggestions in the text box
                            tinyDB.putListString("groupedFarmers", farmers_list);
                            tinyDB.putListString("groupedProducts", products_list);

                            for (int z = 0; z < farmers_list.size(); z++) {
                                Log.d("FARMER_LIST", farmers_list.get(z).toString());
                            }

                            for (int y = 0; y < products_list.size(); y++) {
                                Log.d("Products_List", products_list.get(y).toString());
                            }
                        }
                    }
                });
            }
        });
    }
}

// TODO: Display spinner when processing requests

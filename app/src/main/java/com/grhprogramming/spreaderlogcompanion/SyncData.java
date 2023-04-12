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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SyncData extends AppCompatActivity {

    private TextView txtConnection;
    private Button btnSync;
    private String connection;
    private TextView txtCount;
    private DbHandler db;
    private Integer dataIndex;
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
        txtCount = findViewById(R.id.text_count_label);
        // progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);


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
                OkHttpClient okHttpClient1 = new OkHttpClient();
                DataModel dataModel;

                while (db.getQueryCount() > 0) {
                    Log.i("TESTING", "Query Count: " + db.getQueryCount());
                    Log.i("TESTING", "Loop");
                    dataModel = db.getFirst();
                    Log.i("TESTING", "Name: " + dataModel.getFarmer());
                    dataIndex = Integer.parseInt(dataModel.getId());
                    Log.i("TESTING", "Data Index: " + dataIndex);

                    RequestBody formbody
                            = new FormBody.Builder()
                            .add("rUser", "17469GRH")
                            .add("rDate", dataModel.getDate())
                            .add("rFarmer", dataModel.getFarmer())
                            .add("rField", dataModel.getField())
                            .add("rProduct", dataModel.getProduct())
                            .add("rAcres", dataModel.getAcres())
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

                            } else if (response.body().string().equals("error")) {
                                Log.i("TESTING", "Companion Error!");
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
    }
}

// TODO: Display spinner when processing requests

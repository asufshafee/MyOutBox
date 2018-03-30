package com.example.manzoorhussain.myoutbox;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manzoorhussain.myoutbox.Objects.User;
import com.example.manzoorhussain.myoutbox.Requests.JSONParser;
import com.example.manzoorhussain.myoutbox.Service.SmsService;
import com.example.manzoorhussain.myoutbox.Session.MyClass;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactFromExcel extends AppCompatActivity {

    List<User.Sheet1Object> LisObjects;
    Boolean Check = false;
    EditText Message, Url;
    MyClass myClass;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_from_excel);
        LisObjects = new ArrayList<>();
        Url = (EditText) findViewById(R.id.Url);
        Message = (EditText) findViewById(R.id.Message);
        myClass = (MyClass) getApplicationContext();

        findViewById(R.id.Browse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Url.getText().toString().equals("")) {
                    MyClass.setUrl(Url.getText().toString());
                    if (InternetConnection.checkConnection(getApplicationContext())) {


                        dialog = new ProgressDialog(ContactFromExcel.this);
                        dialog.setTitle("Hey Wait Please...");
                        dialog.setMessage("");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.show();
                            }
                        });

                        new GetDataTask().execute();
                    } else {

                        Toast.makeText(getApplicationContext(), "No internet ", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Url ", Toast.LENGTH_LONG).show();

                }

            }
        });

        findViewById(R.id.Send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Check) {
                    for (User.Sheet1Object sheet1Object : LisObjects) {
                        sheet1Object.setMesasege(Message.getText().toString());
                    }
                    MyClass myClass = (MyClass) getApplicationContext();
                    myClass.setSheet1Objects(LisObjects);

                    if (!isMyServiceRunning(SmsService.class)) {
                        Intent intent = new Intent(getApplicationContext(), SmsService.class);
                        startService(intent);

                        Toast.makeText(getApplicationContext(),
                                "SMS Sending Started..",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Already is Process ", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Import Contacts First", Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    /**
     * Creating Get Data Task for Getting Data From Web
     */
    class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            try {
                JSONObject jsonObject = JSONParser.getDataFromWeb();
                Gson gson = new Gson();
                User user = new User();
                user = gson.fromJson(jsonObject.toString(), User.class);
                LisObjects = user.getSheet1();
                Check = true;
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Imported ", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception Ex) {
                Toast.makeText(getApplicationContext(), Ex.getMessage(), Toast.LENGTH_LONG).show();

                Ex.getStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

        }
    }

    public static class InternetConnection {

        /**
         * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
         */
        public static boolean checkConnection(@NonNull Context context) {
            return ((ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}

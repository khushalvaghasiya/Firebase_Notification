package com.example.myfirebasenotification.ui.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.notdemo.SendNotificationPack.*;

import com.example.myfirebasenotification.R;
import com.example.myfirebasenotification.api.APIService;
import com.example.myfirebasenotification.api.Client;
import com.example.myfirebasenotification.models.Data;
import com.example.myfirebasenotification.models.MyResponse;
import com.example.myfirebasenotification.models.NotificationSender;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotif extends AppCompatActivity {
    EditText Title, Message;
    Button send;
    String TAG = "mylog";
    String fcmurl = "https://fcm.googleapis.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Title = findViewById(R.id.etTitle);
        Message = findViewById(R.id.etMessage);
        send = findViewById(R.id.btnNotification);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usertoken="czRWXWdxRJCXvvhku4Kdkv:APA91bGvPPnfQUG13p7X6CfAZke8OXu1FCgsrwMkj9Zp52xmME3KOBmGc1O0R-ZyE8lVH-djG97bJRI_3vo6ah5PfA7C60LBzlXJ1VHb3xavfQ23r2cKRXsSoSEZ4IsYRe79DYpsjPon";
                sendNotifications(usertoken, Title.getText().toString().trim(), Message.getText().toString().trim());
               /* APIService apiService1;
                apiService1 = Client_wamp.getClient("http://192.168.0.7/").create(APIService.class);
                apiService1.getKey().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String usertoken = response.body();
                        sendNotifications(usertoken, Title.getText().toString().trim(), Message.getText().toString().trim());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("mylog", t.toString());
                    }
                });*/
            }
        });
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);

        NotificationSender sender = new NotificationSender(data, usertoken);

        APIService apiService = Client.getClient(fcmurl).create(APIService.class);

        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(SendNotif.this, "Failed ", Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(SendNotif.this, "notification has been sent...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}

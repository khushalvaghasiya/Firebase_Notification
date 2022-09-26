package com.example.myfirebasenotification.ui.activities;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.SEND_SMS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirebasenotification.R;
import com.example.myfirebasenotification.api.APIService;
import com.example.myfirebasenotification.api.Client;
import com.example.myfirebasenotification.models.Data;
import com.example.myfirebasenotification.models.MyResponse;
import com.example.myfirebasenotification.models.NotificationSender;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String token;
    Button btnToken,btnMessage,btnNotification;
    TextView mToken;
    EditText etTitle,etMessage;
    String fcmurl = "https://fcm.googleapis.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       requestPermission();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        mToken=findViewById(R.id.tvToken);
        btnToken=findViewById(R.id.btnToken);
        btnMessage=findViewById(R.id.btnSend);
        btnNotification=findViewById(R.id.btnNotification);
        etTitle=findViewById(R.id.etTitle);
        etMessage=findViewById(R.id.etMessage);
        btnToken.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {

                            if (!task.isSuccessful()) {
                                //handle token error
                                return;
                            }

                            token = task.getResult();
                            //mToken.setText((token));
                            etMessage.setText((token));
                            Log.d("", "onCreate mine: " + token);

                        });
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                requestPermission();
                etTitle.setText("SMS Sent");
                String smsText;
                smsText="HELP REQUIRED \n HBL Korangi Branch Karachi";
                SmsManager smsManager =SmsManager.getDefault();
                try {
                    smsManager.sendTextMessage("8000028937",null,smsText,null,null);
                    Toast.makeText(getApplicationContext(),"SMS Sent",Toast.LENGTH_LONG).show();
                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(),String.valueOf(ex.getMessage()),Toast.LENGTH_LONG).show();
                }

            }
        });
        btnNotification.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
               // String usertoken="czRWXWdxRJCXvvhku4Kdkv:APA91bGvPPnfQUG13p7X6CfAZke8OXu1FCgsrwMkj9Zp52xmME3KOBmGc1O0R-ZyE8lVH-djG97bJRI_3vo6ah5PfA7C60LBzlXJ1VHb3xavfQ23r2cKRXsSoSEZ4IsYRe79DYpsjPon";
                String usertoken="eAArLTAcSs2Bh-dHu-f8zL:APA91bFtzf6x8Cbgmtr4KZgpCkqhSnxX08ltJ4ZfTntWjcXsozd6o3Udauea64vpOmhCAFT01NxGJ6u0wmWq-hbwpe9smZhuSkLiRb_LgZtwF2wSdeFbQ5RlahNTP5g2yMgGyp2Z0HrB";
                sendNotifications(usertoken, etTitle.getText().toString().trim(), etMessage.getText().toString().trim());
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
                Toast.makeText(MainActivity.this,response.code(),Toast.LENGTH_LONG).show();
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(MainActivity.this, "Failed ", Toast.LENGTH_LONG);
                    } else {
                       // Toast.makeText(MainActivity.this, "notification has been sent...", Toast.LENGTH_LONG).show();
                        System.out.println("Notification sent....");
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
    public static final int PERMISSION_REQUEST_CODE = 1245;
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, SEND_SMS);
        int result_location = ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION);
        int result_locations = ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result_location == PackageManager.PERMISSION_GRANTED && result_locations == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{SEND_SMS, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }
}
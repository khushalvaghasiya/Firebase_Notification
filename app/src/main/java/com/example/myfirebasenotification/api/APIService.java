package com.example.myfirebasenotification.api;

import com.example.myfirebasenotification.models.MyResponse;
import com.example.myfirebasenotification.models.NotificationSender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAVyATFls:APA91bF3FO2HP3h71mNVGtbcACddcRnz6sZBrUubsRCUyGzB_bg3o5W9TUxLR6q36Pif6CYdpsYtJ8Q6Tq8XTjsvWu7m2QSKpnVGYWMlRr7sladBejug-F-4YGugDXbRVLGHIwQgMI67"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);


}

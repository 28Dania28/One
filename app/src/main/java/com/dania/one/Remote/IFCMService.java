package com.dania.one.Remote;
import com.dania.one.Model.FCMResponse;
import com.dania.one.Model.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAEMZ4ojc:APA91bFtHeOZVHCG2GY21F9CWOgIWhbb2wH330jLPScw3GDFKrfD6_A8vM5Ai3dn-sIc1kHddITPQ2NjRr8GH5tWihi_MYZI-BW_W0G5mxkG8nby-zUkZO5qs2i6K-EQbfEKsqs4Czvr"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}

//Obersavable should be of io.reactivex

//FCMResponse
//FCMResult
//FCMSendData
//RetrofitFCMClient
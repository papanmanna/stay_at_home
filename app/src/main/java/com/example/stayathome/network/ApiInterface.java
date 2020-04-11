package com.example.stayathome.network;


import com.example.stayathome.models.CreateRequestResponse;
import com.example.stayathome.models.GetRequestResponse;
import com.example.stayathome.models.LogInResponse;
import com.example.stayathome.models.MeResponse;
import com.example.stayathome.models.RegistrationResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("v1/login")
    Observable<Response<LogInResponse>> login(@Field("phone") String phone, @Field("password") String password);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("v1/users/signup")
    Observable<Response<RegistrationResponse>> signUp(@Field("userName") String userName, @Field("email") String email, @Field("password") String password,
                                                      @Field("phone") String phone, @Field("aadharNumber") String aadhaarNo, @Field("pinCode") String pinCode);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("v1/me")
    Observable<Response<MeResponse>> me(@Header("Authorization") String authkey);


    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("v1/users/requests")
    Observable<Response<GetRequestResponse>> getAllRequest(@Header("Authorization") String authkey);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("v1/users/requests")
    Observable<Response<CreateRequestResponse>> create(@Header("Authorization") String authkey, @Field("stationId") String stationId, @Field("reason") String reason,
                                                       @Field("date") long date, @Field("startHour") int startHour, @Field("endHour") int endHour, @Field("personCount") int count);


}

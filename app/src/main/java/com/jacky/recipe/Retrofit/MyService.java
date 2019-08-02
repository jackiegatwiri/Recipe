package com.jacky.recipe.Retrofit;

import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyService {
    @POST("/users/register")
    @FormUrlEncoded //observable in RxJava is a class that emits a stream of data or events
    Observable<String> registerUser(@Field("name")String name,
                                    @Field("email")String email,
                                    @Field("password")String password,
                                    @Field("password2")String password2);

    @POST("/users/login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email")String email,
                                    @Field("password")String password);
}


package com.hopeTheRobot.network;

import com.hopeTheRobot.pojo.Covid19ReportItem;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ApiClient {
    private String BASE_URL = "https://disease.sh/v3/covid-19/";
    private Covid19Interface covid19Interface;
    private static ApiClient INSTANCE;


    public ApiClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        covid19Interface = retrofit.create(Covid19Interface.class);

    }

    public static ApiClient getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ApiClient();
        }
        return INSTANCE;
    }


    public Call<Covid19ReportItem> getGeneralReport(){
        return covid19Interface.getGeneralReport();
    }


    public Call<Covid19ReportItem> getOneCountryReport(String countryName){
        return covid19Interface.getOneCountryReport(countryName);
    }

}

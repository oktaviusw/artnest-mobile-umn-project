package id.ac.umn.mobile.myapplication;

import android.provider.Settings;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Oktavius Wiguna on 09/05/2018.
 */
/*Ini copas total aja ud paten*/
public class APIClient {
    public static Retrofit retrofit = null;

    //create connection
    public static Retrofit getApiClient(){
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(loggingInterceptor);

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://artnest-umn.000webhostapp.com/API/") //BASE URL TARO SINI
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();
        }
        return retrofit;
    }
}

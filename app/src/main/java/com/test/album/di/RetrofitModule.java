package com.test.album.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.album.network.AlbumApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.test.album.utility.AppUrls.BASE_URL;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
@Module(includes = OkHttpClientModule.class)
public class RetrofitModule {


    @Provides
    @Singleton
    public AlbumApi getAlbumsService(Retrofit getClient) {
        return getClient.create(AlbumApi.class);
    }

    @Provides
    public Retrofit getClient(OkHttpClient okHttpClient,
                              GsonConverterFactory gsonConverterFactory, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }
}


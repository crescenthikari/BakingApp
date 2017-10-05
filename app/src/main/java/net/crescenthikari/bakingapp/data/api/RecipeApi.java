package net.crescenthikari.bakingapp.data.api;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Muhammad Fiqri Muthohar on 9/18/17.
 */

public class RecipeApi {
    private static Gson gson;
    private static OkHttpClient client;

    private static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private static File getCacheDirectory() {
        File dir = new File("cache");
        return dir;
    }

    private static OkHttpClient getClient() {
        if (client == null) {
            final long cacheSize = 100 * 1024L * 1024; // 100 MB Cache
            Cache cache = new Cache(getCacheDirectory(), cacheSize);
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl url = request.url().newBuilder()
                                    .build();
                            request = request.newBuilder().url(url).build();

                            return chain.proceed(request);
                        }
                    }).build();
        }
        return client;
    }

    public static RecipeService open() {
        OkHttpClient client = getClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecipeServiceConstant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(RecipeService.class);
    }
}

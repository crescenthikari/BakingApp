package net.crescenthikari.bakingapp.di.module;

import com.google.gson.Gson;

import net.crescenthikari.bakingapp.data.api.RecipeService;
import net.crescenthikari.bakingapp.data.api.RecipeServiceConstant;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
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
 * Created by Muhammad Fiqri Muthohar on 8/8/17.
 */
@Module
public class ApiModule {

    @Provides
    @Named("base_url")
    String provideBaseUrl() {
        return RecipeServiceConstant.BASE_URL;
    }

    @Provides
    File provideCacheDir() {
        return new File("cache");
    }

    @Provides
    Cache provideCache(File cacheDir) {
        final long cacheSize = 100 * 1024L * 1024; // 100 MB Cache
        return new Cache(cacheDir, cacheSize);
    }

    @Provides
    OkHttpClient provideHttpClient(Cache cache) {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
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

    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    RecipeService provideApi(@Named("base_url") String baseUrl,
                             OkHttpClient client,
                             GsonConverterFactory gsonConverterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(RecipeService.class);
    }
}

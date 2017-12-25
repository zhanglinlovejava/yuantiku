package com.lin.yuantiku.injector.module;

import android.content.Context;

import com.lin.yuantiku.BuildConfig;
import com.lin.yuantiku.api.base.AsyncRxJavaCallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhanglin on 2017/7/25.
 */
@Module
public class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideApplicationContext() {
        return context.getApplicationContext();
    }


    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS); // 设置连接超时时间
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String url = original.url().url().toString();
                Request.Builder newReuestBuilder = original.newBuilder();
                newReuestBuilder.method(original.method(), original.body());
                newReuestBuilder.url(url);
//                if (!TextUtils.isEmpty(userStorage.getToken())) {
//                    newReuestBuilder.addHeader("Authorization", "Bearer " + userStorage.getToken());
//                    if (userStorage.isTokenExpires()) {
//                        Logger.e("token 过期了");
//                    }
//                }
                newReuestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                return chain.proceed(newReuestBuilder.build());
            }
        });
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(logging);
        }
        return okHttpBuilder.build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://www.baidu.com")// TODO: 2017/7/25
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(AsyncRxJavaCallAdapterFactory.create())
                .build();
    }
}

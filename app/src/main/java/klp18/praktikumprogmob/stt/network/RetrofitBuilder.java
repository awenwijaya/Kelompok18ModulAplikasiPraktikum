package klp18.praktikumprogmob.stt.network;

import com.facebook.stetho.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import klp18.praktikumprogmob.stt.TokenManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitBuilder {

    private static final String BASE_URL = "http://192.168.43.149/newfolder2/laravel-progmob-api-test/public/api/";
    private static final String base_url = "http://192.168.43.149/newfolder2/laravel-progmob-api-test/crud-php/";
    private final static OkHttpClient client = buildClient();
    private final static Retrofit retrofit = buildRetrofit(client);
    private static Retrofit retrofitPHP = null;
    private static Retrofit Retrofit = null;

    private static OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder()
                                .addHeader("Accept", "application/json")
                                .addHeader("Connection", "close");
                        request = builder.build();

                        return chain.proceed(request);
                    }
                });

        if(BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        return builder.build();

    }

    public static Retrofit getClientPHP() {
        if(retrofitPHP == null) {
            retrofitPHP = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitPHP;
    }

    public static Retrofit getClient() {
        if(Retrofit == null) {
            Retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return Retrofit;
    }

    private static Retrofit buildRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public static <T> T createService(Class <T> service) {
        return retrofit.create(service);
    }



    public static <T> T createServiceWithAuth(Class<T> service, TokenManager tokenManager) {

        OkHttpClient newClient = client.newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                if(tokenManager.getToken().getAccessToken() != null) {
                    builder.addHeader("Authorization", "Bearer" + tokenManager.getToken().getAccessToken());
                }
                request = builder.build();
                return chain.proceed(request);
            }
        }).build();

        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
        return newRetrofit.create(service);

    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

}

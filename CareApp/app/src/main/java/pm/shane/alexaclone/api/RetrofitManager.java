package pm.shane.alexaclone.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pm.shane.alexaclone.SessionManager;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shane on 21/10/2017.
 */

public class RetrofitManager {

    private static OkHttpClient webClient;
    private static Retrofit retrofit;
    private static CareApi service;

    public static synchronized Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getWebClient())
                    .baseUrl(CareApi.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static synchronized OkHttpClient getWebClient() {
        if (webClient == null) {
            webClient = new OkHttpClient.Builder()
                    .addInterceptor(RetrofitManager::interceptAddHeaders)
                    .cookieJar(SessionManager.getCookieJar())
                    .build();
        }
        return webClient;
    }

    public static Response interceptAddHeaders(Interceptor.Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Origin", chain.request().url().toString())
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();

        return chain.proceed(request);
    }

    public static synchronized CareApi getService() {
        if (service == null) {
            service = getRetrofit().create(CareApi.class);
        }
        return service;
    }

}

package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager;

import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sugitatakuto on 2017/02/20.
 */
public class ApiManager {
    private static ApiService apiService;
    static volatile Retrofit retrofit = null;

    private ApiManager(){}

    public static ApiService getApiService() {
        initApiService();
        return apiService;
    }

    private static void initApiService() {
        if(apiService == null) {
            synchronized (ApiManager.class) {
                if(apiService == null) {
                    apiService = getRetrofit().create(ApiService.class);
                }
            }
        }
    }

    private static Retrofit getRetrofit() {
        if(retrofit == null) {
            synchronized (ApiManager.class) {
                if(retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(CommonConst.Api.LOCAL_SERVER_FOR_RX)
                            .build();
                }
            }
        }
        return retrofit;
    }
}

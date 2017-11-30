package com.dxngxhl.network.retorfit;

import retrofit2.Retrofit;

/**
 * Created by dxngxhl on 2017/11/30.
 */
/**
 *
 * */
public class RetrofitService {
    private static WMRetrofitApi wmRetrofitApi = null;
    public static WMRetrofitApi getInstance(){
        if (wmRetrofitApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("")
                    .build();
            wmRetrofitApi = retrofit.create(WMRetrofitApi.class);
        }
        return  wmRetrofitApi;
    }
}

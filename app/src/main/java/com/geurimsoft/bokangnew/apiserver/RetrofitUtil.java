/**
 * Retrofit 서비스 생성 모듈
 *
 * 2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 *
 */

package com.geurimsoft.bokangnew.apiserver;

import android.util.Log;
import com.geurimsoft.data.GSConfig;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil
{

    private static final String BASE_URL = GSConfig.API_SERVER_ADDR;

    /**
     * API 서버 주소를 이용한 레트로핏 빌드
     * @return RetrofitService 호출
     */
    public static RetrofitService getService()
    {

        String functionName = "getService()";

        Log.d(GSConfig.APP_DEBUG, RetrofitUtil.class.getName() + " : " + functionName);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RetrofitService.class);

    }

}
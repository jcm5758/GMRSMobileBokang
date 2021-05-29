/**
 * API 서비스 정의
 *
 * 2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 */

package com.geurimsoft.bokangnew.apiserver;

import com.geurimsoft.bokangnew.apiserver.data.RequestData;
import com.geurimsoft.bokangnew.apiserver.data.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService
{

    /**
     * 로그인 메소드
     * @param jsonObject 로그인 할 id, pw
     * @return status, result 결과와 사용자 정보
     */
    @POST("API")
    Observable<UserInfo> apiLogin(@Body RequestData jsonObject);

    /**
     * 일별 데이터 (Unit TotolPrice)
     * @param jsonObject branchID, serchDate, qryContent
     * @return status, result 결과 정보
     */
    @POST("API")
    Observable<UserInfo> getDay(@Body RequestData jsonObject);

}
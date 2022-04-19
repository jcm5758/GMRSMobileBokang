/**
 * 일보 수량
 *
 * 2021. 05. 28. 리뉴얼
 *
 *  Written by jcm5758
 *
 */

package com.geurimsoft.bokangnew.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.util.GSUtil;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOut;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentDaily extends Fragment
{

	private LinearLayout income_empty_layout, release_empty_layout, petosa_empty_layout;
	private LinearLayout income_empty_layout_outside_source, income_empty_layout_outside_product;
	private LinearLayout release_empty_layout_outside_source, release_empty_layout_outside_product;

	private TextView stats_daily_date, daily_income_title, daily_release_title, daily_petosa_title;
	private TextView daily_income_title_outside_source, daily_income_title_outside_product;
	private TextView daily_release_title_outside_source, daily_release_title_outside_product;

	// 수량, 금액 타입
	private int stateType = GSConfig.STATE_AMOUNT;

	private String qryContent = "Unit";

	public FragmentDaily(int stateType, String qryContent)
	{
		this.stateType = stateType;
		this.qryContent = qryContent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.statviewdetaildaily, container, false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();

		String functionName = "onResume()";
		
		View view = this.getView();

		if(view == null)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " View is null");
			return;
		}

		// 입고 빈 레이아웃
		this.income_empty_layout = (LinearLayout)view.findViewById(R.id.income_empty_layout);

		// 출고 빈 레이아웃
		this.release_empty_layout = (LinearLayout)view.findViewById(R.id.release_empty_layout);

		// 토사 빈 레이아웃
		this.petosa_empty_layout = (LinearLayout)view.findViewById(R.id.petosa_empty_layout);

		// 외부입고(원석) 빈 레이아웃
		this.income_empty_layout_outside_source = (LinearLayout)view.findViewById(R.id.income_empty_layout_outside_source);

		// 외부입고(제품) 빈 레이아웃
		this.income_empty_layout_outside_product = (LinearLayout)view.findViewById(R.id.income_empty_layout_outside_product);

		// 외부출고(원석) 빈 레이아웃
		this.release_empty_layout_outside_source = (LinearLayout)view.findViewById(R.id.release_empty_layout_outside_source);

		// 외부출고(제품) 빈 레이아웃
		this.release_empty_layout_outside_product = (LinearLayout)view.findViewById(R.id.release_empty_layout_outside_product);

		// 제목 텍스트 뷰
		this.stats_daily_date = (TextView)view.findViewById(R.id.stats_daily_date);

		// 입고 텍스트 뷰
		this.daily_income_title = (TextView) view.findViewById(R.id.daily_income_title);

		// 출고 텍스트 뷰
		this.daily_release_title = (TextView) view.findViewById(R.id.daily_release_title);

		// 토사 텍스트 뷰
		this.daily_petosa_title = (TextView) view.findViewById(R.id.daily_petosa_title);

		// 외부입고(원석) 텍스트 뷰
		this.daily_income_title_outside_source = (TextView) view.findViewById(R.id.daily_income_title_outside_source);

		// 외부입고(제품) 텍스트 뷰
		this.daily_income_title_outside_product = (TextView) view.findViewById(R.id.daily_income_title_outside_product);

		// 외부출고(원석) 텍스트 뷰
		this.daily_release_title_outside_source = (TextView) view.findViewById(R.id.daily_release_title_outside_source);

		// 외부출고(제품) 텍스트 뷰
		this.daily_release_title_outside_product = (TextView) view.findViewById(R.id.daily_release_title_outside_product);

		// 일일 입고/출고/토사 수량 조회
		makeData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH,GSConfig.DAY_STATS_DAY);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}

    /**
     * 일일 입고/출고/토사 수량 조회
	 *
	 * @param _year				연도
	 * @param _monthOfYear		월
	 * @param _dayOfMonth		일
	 */
	public void makeData(int _year, int _monthOfYear, int _dayOfMonth)
	{

		String functionName = "makeData()";

		try
		{

			String str = _year + "년 " + _monthOfYear + "월 " + _dayOfMonth + "일 입출고 현황";
//			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + _year + "년 " + _monthOfYear + "월 " + _dayOfMonth + "일");

			this.stats_daily_date.setText(str);

			String queryDate = GSUtil.makeStringFromDate(_year, _monthOfYear, _dayOfMonth);

			this.getData(queryDate);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}
	
	private void getData(String searchDate)
	{

		String functionName = "getData()";

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "searchDate : " + searchDate + ", qryContent : " + qryContent);

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				//응답을 잘 받았을 때 이 메소드가 자동으로 호출
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
//						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);
						parseData(response);
					}
				},
				//에러 발생시 호출될 리스너 객체
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "에러 -> " + error.getMessage());
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("GSType", "DAY");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + ", \"SearchDate\": " + searchDate + ", \"QryContent\" : \"" + qryContent + "\" }");
				return params;
			}
		};

		request.setRetryPolicy(new DefaultRetryPolicy(
				0,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setShouldCache(false);
		requestQueue.add(request);

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "요청 보냄.");

	}

	public void parseData(String msg)
	{

		String functionName = "parseData()";

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + msg);

		GSDailyInOut dio = new GSDailyInOut();

		Gson gson = new Gson();

		GSDailyInOutGroup[] diog = gson.fromJson(msg, GSDailyInOutGroup[].class);

		dio.list = new ArrayList<>(Arrays.asList(diog));

		this.setDisplayData(dio);

	}

	/**
	 *
	 * 표 형식으로 데이터 표출하기
	 *
	 * @param dio	데이터 그룹
	 *
	 */
	private void setDisplayData(GSDailyInOut dio)
	{

		if(getActivity() == null)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplayData() : Activity is null.");
			return;
		}

		if (dio == null)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplayData() : dio is null.");
			return;
		}

		GSDailyInOutGroup tempGroup = null;

		//--------------------------------------------------
		// 레이아웃 초기화
		//--------------------------------------------------

		income_empty_layout.removeAllViews();
		release_empty_layout.removeAllViews();
		petosa_empty_layout.removeAllViews();
		income_empty_layout_outside_source.removeAllViews();
		income_empty_layout_outside_product.removeAllViews();
		release_empty_layout_outside_source.removeAllViews();
		release_empty_layout_outside_product.removeAllViews();

		//---------------------------------------------------------------------------------
		// 표 생성 클래스 초기화
		//---------------------------------------------------------------------------------

		DailyStatsView statsView = new DailyStatsView(getActivity(), this.stateType);

		//---------------------------------------------------------------------------------
		// 입고 데이터 표출
		//---------------------------------------------------------------------------------

		tempGroup = dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] );
		if (tempGroup != null && tempGroup.List.size() > 0)
		{
			statsView.makeView(income_empty_layout, tempGroup);
			daily_income_title.setText(tempGroup.getTitle(this.stateType));
		}
		else
		{
			daily_income_title.setVisibility(View.GONE);
		}

		//---------------------------------------------------------------------------------
		// 출고 데이터 표출
		//---------------------------------------------------------------------------------

		tempGroup = dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] );
		if (tempGroup != null && tempGroup.List.size() > 0)
		{
			statsView.makeView(release_empty_layout, tempGroup);
			daily_release_title.setText(tempGroup.getTitle(this.stateType));
		}
		else
		{
			daily_release_title.setVisibility(View.GONE);
		}

		//---------------------------------------------------------------------------------
		// 토사 데이터 표출
		//---------------------------------------------------------------------------------

		tempGroup = dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] );
		if (tempGroup != null && tempGroup.List.size() > 0)
		{
			statsView.makeView(petosa_empty_layout, tempGroup);
			daily_petosa_title.setText(tempGroup.getTitle(this.stateType));
		}
		else
		{
			daily_petosa_title.setVisibility(View.GONE);
		}

		//---------------------------------------------------------------------------------
		// 외부입고(원석) 데이터 표출
		//---------------------------------------------------------------------------------

		tempGroup = dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] );
		if (tempGroup != null && tempGroup.List.size() > 0)
		{
			statsView.makeView(income_empty_layout_outside_source, tempGroup);
			daily_income_title_outside_source.setText(tempGroup.getTitle(this.stateType));
		}
		else
		{
			daily_income_title_outside_source.setVisibility(View.GONE);
		}

		//---------------------------------------------------------------------------------
		// 외부입고(제품) 데이터 표출
		//---------------------------------------------------------------------------------

		tempGroup = dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] );
		if (tempGroup != null && tempGroup.List.size() > 0)
		{
			statsView.makeView(income_empty_layout_outside_product, tempGroup);
			daily_income_title_outside_product.setText(tempGroup.getTitle(this.stateType));
		}
		else
		{
			daily_income_title_outside_product.setVisibility(View.GONE);
		}

		//---------------------------------------------------------------------------------
		// 외부출고(원석) 데이터 표출
		//---------------------------------------------------------------------------------

		tempGroup = dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_SOURCE] );
		if (tempGroup != null && tempGroup.List.size() > 0)
		{
			statsView.makeView(release_empty_layout_outside_source, tempGroup);
			daily_release_title_outside_source.setText(tempGroup.getTitle(this.stateType));
		}
		else
		{
			daily_release_title_outside_source.setVisibility(View.GONE);
		}

		//---------------------------------------------------------------------------------
		// 외부출고(제품) 데이터 표출
		//---------------------------------------------------------------------------------

		tempGroup = dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT] );
		if (tempGroup != null && tempGroup.List.size() > 0)
		{
			statsView.makeView(release_empty_layout_outside_product, tempGroup);
			daily_release_title_outside_product.setText(tempGroup.getTitle(this.stateType));
		}
		else
		{
			daily_release_title_outside_product.setVisibility(View.GONE);
		}

	}

}

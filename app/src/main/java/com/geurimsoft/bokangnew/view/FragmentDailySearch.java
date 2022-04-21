/**
 * 일보 수량
 *
 * 2021. 05. 28. 리뉴얼
 *
 *  Written by jcm5758
 *
 */

package com.geurimsoft.bokangnew.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOut;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.GSCustomerPriceGroup;
import com.geurimsoft.bokangnew.data.GSSimpleArray;
import com.geurimsoft.bokangnew.util.GSUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentDailySearch extends Fragment
{

	private TextView tvDailySearchTitle;
	private EditText etCustomerName;
	private ListView lvCustomerListView;

	// original 데이터
	private ArrayList customerPriceList = new ArrayList();

	// 리스트뷰용 데이터
	private List<String> lvList = new ArrayList();

	// 리스트뷰용 어댑터
	private ArrayAdapter<String> lvAdapter;

	private boolean isDebugging = true;

	public FragmentDailySearch() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{

		String functionName = "onCreateView()";

		View view = inflater.inflate(R.layout.daily_search, container, false);

		this.tvDailySearchTitle = (TextView)view.findViewById(R.id.tvDailySearchTitle);
		this.etCustomerName = (EditText) view.findViewById(R.id.etCustomerName);
		this.lvCustomerListView = (ListView) view.findViewById(R.id.lvCustomerListView);

		this.etCustomerName.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void afterTextChanged(Editable editable)
			{
				String text = etCustomerName.getText().toString();
				search(text);
			}

		});

		getData("", "", "");

		return view;

	}

	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	@Override
	public void onResume() { super.onResume(); }

	/**
	 *
	 * 거래처 단가 조회 하기
	 *
	 * @param customerName			거래처명
	 * @param customerSiteName		현장명
	 * @param product				품명
	 */
	private void getData(String customerName, String customerSiteName, String product)
	{

		String functionName = "getData()";

		if (isDebugging)
			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "customerName : " + customerName + ", customerSiteName : " + customerSiteName+ ", product : " + product);

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				//응답을 잘 받았을 때 이 메소드가 자동으로 호출
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						if (isDebugging)
							Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);

						Gson gson = new Gson();

						GSCustomerPriceGroup customerPriceGroup = gson.fromJson(response, GSCustomerPriceGroup.class);

						if (isDebugging)
							Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "Status : " + customerPriceGroup.Status);

						if (customerPriceGroup.Status.equals("OK"))
						{

							// 리스트뷰용 데이터
							lvList = customerPriceGroup.List;

							// 보관용 데이터
							customerPriceList.addAll(lvList);

							lvAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lvList);
							lvCustomerListView.setAdapter(lvAdapter);

						}
						else
						{
							Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "Status : " + customerPriceGroup.Message);
							return;
						}

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
				params.put("GSType", "SEARCH_CUSTOMERPRICE");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID()
						+ ", \"CustomerName\": \"" + customerName + "\", \"CustomerSiteName\" : \"" + customerSiteName + "\", \"Product\" : \"" + product + "\" }");
				return params;
			}
		};

		request.setShouldCache(false);
		requestQueue.add(request);

	}

	// 검색을 수행하는 메소드
	public void search(String charText)
	{

		// 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
		this.lvList.clear();

		// 문자 입력이 없을때는 모든 데이터를 보여준다.
		if (charText.length() == 0) {
			this.lvList.addAll(this.customerPriceList);
		}
		// 문자 입력을 할때..
		else
		{
			// 리스트의 모든 데이터를 검색한다.
			for(int i = 0;i < this.customerPriceList.size(); i++)
			{
				// arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
				if ( ((String)this.customerPriceList.get(i)).contains(charText) )
				{
					// 검색된 데이터를 리스트에 추가한다.
					this.lvList.add((String)this.customerPriceList.get(i));
				}
			}
		}

		// 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
		this.lvAdapter.notifyDataSetChanged();

	}

}

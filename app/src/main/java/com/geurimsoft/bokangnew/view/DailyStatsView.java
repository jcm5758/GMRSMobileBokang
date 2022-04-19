package com.geurimsoft.bokangnew.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOut;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutDetail;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.data.GSConfig;

import java.util.ArrayList;

public class DailyStatsView
{

	private LinearLayout stock_layout, release_layout, petosa_layout;
	private LinearLayout stock_layout_outside_source, stock_layout_outside_product;
	private LinearLayout release_layout_outside_source, release_layout_outside_product;

	private Context mContext;

	private int iUnitMoneyType = 0;

	public DailyStatsView(Context _context, int iUnitMoneyType)
	{

		this.mContext = _context;
		this.iUnitMoneyType = iUnitMoneyType;

	}

	/**
	 * 테이블로 표출
	 * @param _layout
	 */
	public void makeView(LinearLayout _layout, GSDailyInOutGroup data)
	{

		String functionName = "makeView()";

		// 데이터 미존재시 패스
		if(data == null)
		{
			return;
		}

		String[] header = data.Header;
		ArrayList<GSDailyInOutDetail> list = data.List;

		// Layout parameter
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);

		// Header Layout
		for(int header_index = 0; header_index < header.length; header_index++)
		{
			TextView title_textview = makeMenuTextView(mContext, header[header_index], "#ffffff", Gravity.CENTER);
			header_layout.addView(title_textview);
		}

		_layout.addView(header_layout);

		// 본문 레이아웃
		TextView item_textview;

		for(int stock_index = 0; stock_index < list.size(); stock_index++)
		{

			// 상세 정보
			GSDailyInOutDetail diod = list.get(stock_index);

			// 레이아웃
			LinearLayout stock_row_layout = new LinearLayout(mContext);
			stock_row_layout.setLayoutParams(params);
			stock_row_layout.setOrientation(LinearLayout.HORIZONTAL);

			// 가운데 정렬
			int gravity = Gravity.CENTER;

			//-------------------------------------------
			// 거래처명
			//-------------------------------------------

//			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + diod.customerName);

			if( stock_index == (list.size() - 1) )
				item_textview = makeMenuTextView(mContext, diod.customerName, "#000000", gravity);
			else
				item_textview = makeRowTextView(mContext, diod.customerName, gravity);

			stock_row_layout.addView(item_textview);

			//-------------------------------------------
			// 입고 데이터 값 채우기
			//-------------------------------------------

			double[] values = diod.getValues(this.iUnitMoneyType);

			for(int i = 0; i < values.length; i++)
			{

				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

				if( stock_index == (list.size() - 1) )
					item_textview = makeMenuTextView(mContext, GSConfig.changeToCommanString(values[i]), "#000000", gravity);
				else
					item_textview = makeRowTextView(mContext, GSConfig.changeToCommanString(values[i]), gravity);

				stock_row_layout.addView(item_textview);

			}

			_layout.addView(stock_row_layout);

		}

	}

	private TextView makeMenuTextView(Context context, String str, String color, int gravity)
	{

		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		layout_params.weight = 1.0f;

		TextView tv = new TextView(context);
		tv.setLayoutParams(layout_params);
		tv.setGravity(gravity);
		tv.setBackgroundResource(R.drawable.menu_border);
		tv.setPadding(10, 20, 10, 20);
		tv.setTextColor(Color.parseColor(color));
		tv.setTextSize(13);
		tv.setText(str);

		return tv;

	}


	private TextView makeRowTextView(Context context, String str, int gravity)
	{

		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		layout_params.weight = 1.0f;

		TextView tv = new TextView(context);
		tv.setLayoutParams(layout_params);
		tv.setGravity(gravity);
		tv.setBackgroundResource(R.drawable.row_border);
		tv.setPadding(10, 20, 10, 20);
		tv.setTextColor(Color.parseColor("#000000"));
		tv.setTextSize(13);
		tv.setText(str);

		return tv;

	}
}

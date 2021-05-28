package com.geurimsoft.bokangnew.view.etc;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.StatsListData;
import com.geurimsoft.grms.data.GSMonthInOut;
import com.geurimsoft.grms.data.GSMonthInOutDetail;

import java.util.ArrayList;

public class StatsHeaderAndFooterView
{

	private Context mContext;
	private GSMonthInOut monthData;

	private LinearLayout header_layout, footer_layout;

	private int header_count;
	private int footer_count;
	private String[] header_titles;
	private String[] footer_items;

	public StatsHeaderAndFooterView(Context _context, GSMonthInOut monthData, int statType)
	{

		this.mContext = _context;
		this.monthData = monthData;

		GSMonthInOutDetail footerData = this.monthData.getFinalData();
		
		this.header_count = this.monthData.headerCount;
		this.header_titles = this.monthData.header;

		this.footer_count = footerData.valueSize + 1;
		this.footer_items = footerData.getStringValues(statType);

	}

	/**
	 * 통계 테이블의 헤더 부분
	 * @param _header_layout
	 */
	public void makeHeaderView(LinearLayout _header_layout)
	{
		
		this.header_layout = _header_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout layout = new LinearLayout(mContext);
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		// Header Layout
		for(int header_index = 0; header_index < this.header_count; header_index++)
		{
			TextView stock_title_textview = makeMenuTextView(mContext, this.header_titles[header_index], "#ffffff", Gravity.CENTER);
			layout.addView(stock_title_textview);
		}
		
		header_layout.addView(layout);

	}

	/**
	 * 통계 테이블의 맨마지막 합계 부분
	 * @param _footer_layout
	 */
	public void makeFooterView(LinearLayout _footer_layout)
	{
		
		this.footer_layout = _footer_layout;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		LinearLayout layout = new LinearLayout(mContext);
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		TextView textview = null;
		int gravity = Gravity.CENTER;

		for(int footer_index = 0; footer_index < this.footer_count; footer_index++)
		{

			if(footer_index == 0)
				gravity = Gravity.CENTER;
			else
				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

			textview = makeMenuTextView(mContext, this.footer_items[footer_index], "#000000",  gravity);
			layout.addView(textview);

		}

		footer_layout.addView(layout);

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
	
}

package com.geurimsoft.bokangnew.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.grms.data.GSDailyInOut;
import com.geurimsoft.grms.data.GSDailyInOutDetail;
import com.geurimsoft.grms.data.GSDailyInOutGroup;

public class StatsView
{
	
	private LinearLayout stock_layout, release_layout, petosa_layout;
	
	private Context mContext;
	
	private GSDailyInOut dio;
	private int iUnitMoneyType = 0;
	
	private int stock_header_count;
	private int release_header_count;
	private int petosa_header_count;
	
	private String[] stock_header_titles;
	private String[] release_header_titles;
	private String[] petosa_header_titles;
	
	private int stock_count;
	private int release_count;
	private int petosa_count;
	
	private ArrayList<GSDailyInOutDetail> inputList;
	private ArrayList<GSDailyInOutDetail> outputList;
	private ArrayList<GSDailyInOutDetail> slugeList;
	
	public StatsView(Context _context, GSDailyInOut dio, int iUnitMoneyType)
	{

		this.mContext = _context;
		this.dio = dio;
		this.iUnitMoneyType = iUnitMoneyType;

		GSDailyInOutGroup inputGroup = this.dio.findByServiceType("입고");
		if (inputGroup != null)
		{
			this.stock_header_count = inputGroup.headerCount;
			this.stock_header_titles = inputGroup.header;
			this.stock_count = inputGroup.recordCount;
			this.inputList = inputGroup.list;
		}

		GSDailyInOutGroup outputGroup = this.dio.findByServiceType("출고");
		if (outputGroup != null)
		{
			this.release_header_count = outputGroup.headerCount;
			this.release_header_titles = outputGroup.header;
			this.release_count = outputGroup.recordCount;
			this.outputList = outputGroup.list;
		}

		GSDailyInOutGroup slugeGroup = this.dio.findByServiceType("토사");
		if (slugeGroup != null)
		{
			this.petosa_header_count = slugeGroup.headerCount;
			this.petosa_header_titles = slugeGroup.header;
			this.petosa_count = slugeGroup.recordCount;
			this.slugeList = slugeGroup.list;
		}

	}

	/**
	 * 입고 데이터 테이블로 표출
	 * @param _stock_layout
	 */
	public void makeStockStatsView(LinearLayout _stock_layout)
	{

		// 입고 리스트 미존재시 패스
		if(this.inputList == null || this.inputList.size() <= 0)
		{
			Log.d(AppConfig.TAG, "DEBUGGING : " + this.getClass().getName() + " : inputList is null.");
			return;
		}

		// 입고 레이아웃 지정
		this.stock_layout = _stock_layout;

		// Layout parameter
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);

		// Header Layout
		for(int stock_header_index = 0; stock_header_index < this.stock_header_count; stock_header_index++)
		{
			TextView stock_title_textview = makeMenuTextView(mContext, this.stock_header_titles[stock_header_index], "#ffffff", Gravity.CENTER);
			header_layout.addView(stock_title_textview);
		}
		
		stock_layout.addView(header_layout);

		// 본문 레이아웃
		TextView stock_item_textview;
		
		for(int stock_index = 0; stock_index < this.inputList.size(); stock_index++)
		{

			// 상세 정보
			GSDailyInOutDetail diod = inputList.get(stock_index);

			// 레이아웃
			LinearLayout stock_row_layout = new LinearLayout(mContext);
			stock_row_layout.setLayoutParams(params);
			stock_row_layout.setOrientation(LinearLayout.HORIZONTAL);

			// 가운데 정렬
			int gravity = Gravity.CENTER;

			//-------------------------------------------
			// 거래처명
			//-------------------------------------------

			if(stock_index == stock_count - 1)
				stock_item_textview = makeMenuTextView(mContext, diod.customerName, "#000000", gravity);
			else
				stock_item_textview = makeRowTextView(mContext, diod.customerName, gravity);

			stock_row_layout.addView(stock_item_textview);

			//-------------------------------------------
			// 입고 데이터 값 채우기
			//-------------------------------------------

			double[] values = diod.getValues(this.iUnitMoneyType);

			for(int i = 0; i < values.length; i++)
			{
				
				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

				if(stock_index == stock_count - 1)
					stock_item_textview = makeMenuTextView(mContext, AppConfig.changeToCommanString(values[i]), "#000000", gravity);
				else
					stock_item_textview = makeRowTextView(mContext, AppConfig.changeToCommanString(values[i]), gravity);

				stock_row_layout.addView(stock_item_textview);

			}

			stock_layout.addView(stock_row_layout);

		}
		
	}

	/**
	 * 출고 데이터 테이블로 표출
	 * @param _release_layout
	 */
	public void makeReleaseStatsView(LinearLayout _release_layout)
	{

		// 출고 데이터 미존재시 패스
		if(this.outputList == null || this.outputList.size() <= 0)
		{
			Log.d(AppConfig.TAG, "DEBUGGING : " + this.getClass().getName() + " : makeReleaseStatsView : outputList is null.");
			return;
		}

		// 출고 레이아웃 지정
		this.release_layout = _release_layout;

		// Layout parameter
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		// Header Layout
		for(int release_header_index = 0; release_header_index < this.release_header_count; release_header_index++)
		{
			TextView release_title_textview = makeMenuTextView(mContext, this.release_header_titles[release_header_index], "#ffffff", Gravity.CENTER);
			header_layout.addView(release_title_textview);
		}
		
		release_layout.addView(header_layout);

		// 본문 레이아웃
		TextView release_item_textview;
		
		for(int release_index = 0; release_index < this.outputList.size(); release_index++)
		{

			// 상세 정보
			GSDailyInOutDetail diod = this.outputList.get(release_index);

			// 레이아웃
			LinearLayout release_row_layout = new LinearLayout(mContext);
			release_row_layout.setLayoutParams(params);
			release_row_layout.setOrientation(LinearLayout.HORIZONTAL);

			// 가운데 정렬
			int gravity = Gravity.CENTER;

			//-------------------------------------------
			// 거래처명
			//-------------------------------------------

			if(release_index == this.release_count - 1)
				release_item_textview = makeMenuTextView(mContext, diod.customerName, "#000000", gravity);
			else
				release_item_textview = makeRowTextView(mContext, diod.customerName, gravity);

			release_row_layout.addView(release_item_textview);

			//-------------------------------------------
			// 출고 데이터 값 채우기
			//-------------------------------------------

			double[] values = diod.getValues(this.iUnitMoneyType);

			for(int i = 0; i < values.length; i++)
			{

				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

				if(release_index == this.release_count - 1)
					release_item_textview = makeMenuTextView(mContext, AppConfig.changeToCommanString(values[i]), "#000000", gravity);
				else
					release_item_textview = makeRowTextView(mContext, AppConfig.changeToCommanString(values[i]), gravity);

				release_row_layout.addView(release_item_textview);

			}

			release_layout.addView(release_row_layout);

		}
		
	}

	/**
	 * 토사 데이터 테이블로 표출
	 * @param _petosa_layout
	 */
	public void makePetosaStatsView(LinearLayout _petosa_layout)
	{

		// 토사 데이터 미존재시 패스
		if(this.slugeList == null || this.slugeList.size() <= 0)
		{
			Log.d(AppConfig.TAG, "DEBUGGING : " + this.getClass().getName() + " : makePetosaStatsView : slugeList is null.");
			return;
		}

		// 토사 레이아웃 지정
		this.petosa_layout = _petosa_layout;

		// Layout parameter
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);

		// Header Layout
		for(int header_index = 0; header_index < this.petosa_header_count; header_index++)
		{
			TextView release_title_textview = makeMenuTextView(mContext, this.petosa_header_titles[header_index], "#ffffff", Gravity.CENTER);
			header_layout.addView(release_title_textview);
		}

		this.petosa_layout.addView(header_layout);

		// 본문 레이아웃
		TextView item_textview;

		for(int index = 0; index < this.slugeList.size(); index++)
		{

			// 상세 정보
			GSDailyInOutDetail diod = this.slugeList.get(index);

			// 레이아웃
			LinearLayout release_row_layout = new LinearLayout(mContext);
			release_row_layout.setLayoutParams(params);
			release_row_layout.setOrientation(LinearLayout.HORIZONTAL);

			// 가운데 정렬
			int gravity = Gravity.CENTER;

			//-------------------------------------------
			// 거래처명
			//-------------------------------------------

			if(index == this.petosa_count - 1)
				item_textview = makeMenuTextView(mContext, diod.customerName, "#000000", gravity);
			else
				item_textview = makeRowTextView(mContext, diod.customerName, gravity);

			release_row_layout.addView(item_textview);

			//-------------------------------------------
			// 출고 데이터 값 채우기
			//-------------------------------------------

			double[] values = diod.getValues(this.iUnitMoneyType);

			for(int i = 0; i < values.length; i++)
			{

				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

				if(index == this.petosa_count - 1)
					item_textview = makeMenuTextView(mContext, AppConfig.changeToCommanString(values[i]), "#000000", gravity);
				else
					item_textview = makeRowTextView(mContext, AppConfig.changeToCommanString(values[i]), gravity);

				release_row_layout.addView(item_textview);

			}

			this.petosa_layout.addView(release_row_layout);

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

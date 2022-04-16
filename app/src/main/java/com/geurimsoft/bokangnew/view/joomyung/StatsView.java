package com.geurimsoft.bokangnew.view.joomyung;

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

public class StatsView
{

	private LinearLayout stock_layout, release_layout, petosa_layout;
	private LinearLayout stock_layout_outside_source, stock_layout_outside_product;
	private LinearLayout release_layout_outside_source, release_layout_outside_product;

	private Context mContext;

	private GSDailyInOut dio;
	private int iUnitMoneyType = 0;

	private String[] stock_header;
	private String[] release_header;
	private String[] petosa_header;
	private String[] stock_header_outside_source;
	private String[] stock_header_outside_product;
	private String[] release_header_outside_source;
	private String[] release_header_outside_product;

	private ArrayList<GSDailyInOutDetail> inputList;
	private ArrayList<GSDailyInOutDetail> outputList;
	private ArrayList<GSDailyInOutDetail> slugeList;
	private ArrayList<GSDailyInOutDetail> inputOutsideSourceList;
	private ArrayList<GSDailyInOutDetail> inputOutsideProductList;
	private ArrayList<GSDailyInOutDetail> outputOutsideSourceList;
	private ArrayList<GSDailyInOutDetail> outputOutsideProductList;

	public StatsView(Context _context, GSDailyInOut dio, int iUnitMoneyType)
	{

		this.mContext = _context;
		this.dio = dio;
		this.iUnitMoneyType = iUnitMoneyType;

		GSDailyInOutGroup inoutGroup = null;

		//----------------------------------------
		// 입고 데이터
		//----------------------------------------

		inoutGroup = this.dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] );
		if (inoutGroup != null)
		{
			this.stock_header = inoutGroup.Header;
			this.inputList = inoutGroup.List;
		}

		//----------------------------------------
		// 출고 데이터
		//----------------------------------------

		inoutGroup = this.dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] );
		if (inoutGroup != null)
		{
			this.release_header = inoutGroup.Header;
			this.outputList = inoutGroup.List;
		}

		//----------------------------------------
		// 토사 데이터
		//----------------------------------------

		inoutGroup = this.dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] );
		if (inoutGroup != null)
		{
			this.petosa_header = inoutGroup.Header;
			this.slugeList = inoutGroup.List;
		}

		//----------------------------------------
		// 외부입고(원석) 데이터
		//----------------------------------------

		inoutGroup = this.dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] );
		if (inoutGroup != null)
		{
			this.stock_header_outside_source = inoutGroup.Header;
			this.inputOutsideSourceList = inoutGroup.List;
		}

		//----------------------------------------
		// 외부입고(제품) 데이터
		//----------------------------------------

		inoutGroup = this.dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] );
		if (inoutGroup != null)
		{
			this.stock_header_outside_product = inoutGroup.Header;
			this.inputOutsideProductList = inoutGroup.List;
		}

		//----------------------------------------
		// 외부출고(원석) 데이터
		//----------------------------------------

		inoutGroup = this.dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_SOURCE] );
		if (inoutGroup != null)
		{
			this.release_header_outside_source = inoutGroup.Header;
			this.outputOutsideSourceList = inoutGroup.List;
		}

		//----------------------------------------
		// 외부출고(제품) 데이터
		//----------------------------------------

		inoutGroup = this.dio.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT] );
		if (inoutGroup != null)
		{
			this.release_header_outside_product = inoutGroup.Header;
			this.outputOutsideProductList = inoutGroup.List;
		}

	}

	/**
	 * 입고 데이터 테이블로 표출
	 * @param _layout
	 */
	public void makeStockView(LinearLayout _layout)
	{

		String functionName = "makeStockView()";

		// 리스트 미존재시 패스
		if(this.inputList == null || this.inputList.size() <= 0)
		{
			return;
		}

		// 레이아웃 지정
		this.stock_layout = _layout;

		ArrayList<GSDailyInOutDetail> list = this.inputList;
		String[] header = this.stock_header;

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

		this.stock_layout.addView(header_layout);

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

			this.stock_layout.addView(stock_row_layout);

		}

	}

	/**
	 * 출고 데이터 테이블로 표출
	 * @param _layout
	 */
	public void makeReleaseView(LinearLayout _layout)
	{

		String functionName = "makeReleaseView()";

		// 리스트 미존재시 패스
		if(this.outputList == null || this.outputList.size() <= 0)
		{
			return;
		}

		// 레이아웃 지정
		this.release_layout = _layout;

		ArrayList<GSDailyInOutDetail> list = this.outputList;
		String[] header = this.release_header;

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

		this.release_layout.addView(header_layout);

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

			this.release_layout.addView(stock_row_layout);

		}

	}

	/**
	 * 토사 데이터 테이블로 표출
	 * @param _layout
	 */
	public void makePetosaView(LinearLayout _layout)
	{

		String functionName = "makePetosaView()";

		// 리스트 미존재시 패스
		if(this.slugeList == null || this.slugeList.size() <= 0)
		{
			return;
		}

		// 레이아웃 지정
		this.petosa_layout = _layout;

		ArrayList<GSDailyInOutDetail> list = this.slugeList;
		String[] header = this.petosa_header;

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

		this.petosa_layout.addView(header_layout);

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

			this.petosa_layout.addView(stock_row_layout);

		}

	}

	/**
	 * 외부입고(원석) 데이터 테이블로 표출
	 * @param _layout
	 */
	public void makeStockOutsideSourceView(LinearLayout _layout)
	{

		String functionName = "makeStockOutsideSourceView()";

		// 리스트 미존재시 패스
		if(this.inputOutsideSourceList == null || this.inputOutsideSourceList.size() <= 0)
		{
			return;
		}

		// 레이아웃 지정
		this.stock_layout_outside_source = _layout;

		ArrayList<GSDailyInOutDetail> list = this.inputOutsideSourceList;
		String[] header = this.stock_header_outside_source;

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

		this.stock_layout_outside_source.addView(header_layout);

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

			this.stock_layout_outside_source.addView(stock_row_layout);

		}

	}

	/**
	 * 외부입고(제품) 데이터 테이블로 표출
	 * @param _layout
	 */
	public void makeStockOutsideProductView(LinearLayout _layout)
	{

		String functionName = "makeStockOutsideProductView()";

		// 리스트 미존재시 패스
		if(this.inputOutsideProductList == null || this.inputOutsideProductList.size() <= 0)
		{
			return;
		}

		// 레이아웃 지정
		this.stock_layout_outside_product = _layout;

		ArrayList<GSDailyInOutDetail> list = this.inputOutsideProductList;
		String[] header = this.stock_header_outside_product;

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

		this.stock_layout_outside_product.addView(header_layout);

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

			this.stock_layout_outside_product.addView(stock_row_layout);

		}

	}

	/**
	 * 외부출고(원석) 데이터 테이블로 표출
	 * @param _layout
	 */
	public void makeReleaseOutsideSourceView(LinearLayout _layout)
	{

		String functionName = "makeReleaseOutsideSourceView()";

		// 리스트 미존재시 패스
		if(this.outputOutsideSourceList == null || this.outputOutsideSourceList.size() <= 0)
		{
			return;
		}

		// 레이아웃 지정
		this.release_layout_outside_source = _layout;

		ArrayList<GSDailyInOutDetail> list = this.outputOutsideSourceList;
		String[] header = this.release_header_outside_source;

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

		this.release_layout_outside_source.addView(header_layout);

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

			this.release_layout_outside_source.addView(stock_row_layout);

		}

	}

	/**
	 * 외부출고(제품) 데이터 테이블로 표출
	 * @param _layout
	 */
	public void makeReleaseOutsideProductView(LinearLayout _layout)
	{

		String functionName = "makeReleaseOutsideProductView()";

		// 리스트 미존재시 패스
		if(this.outputOutsideProductList == null || this.outputOutsideProductList.size() <= 0)
		{
			return;
		}

		// 레이아웃 지정
		this.release_layout_outside_product = _layout;

		ArrayList<GSDailyInOutDetail> list = this.outputOutsideProductList;
		String[] header = this.release_header_outside_product;

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

		this.release_layout_outside_product.addView(header_layout);

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

			this.release_layout_outside_product.addView(stock_row_layout);

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

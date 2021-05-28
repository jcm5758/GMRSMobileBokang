package com.geurimsoft.data;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.grms.data.GSMonthInOut;
import com.geurimsoft.grms.data.GSMonthInOutDetail;

public class StAdapter extends BaseAdapter
{

	private Context mContext;
	private GSMonthInOut monthData;
	private int requestType;

	private ArrayList<GSMonthInOutDetail>item_list;
	private LayoutInflater mInflater;
	private int header_count = 0;

	public StAdapter(Context _context, GSMonthInOut monthData, int requestType)
	{
		
		this.mContext = _context;
		this.monthData = monthData;
		this.requestType = requestType;

		this.header_count = this.monthData.headerCount;
		this.item_list = this.monthData.getDataWOFinal();

		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public int getCount()
	{
		return this.item_list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return this.item_list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/**
	 * 통계 테이블의 중간 내용
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		
		ViewHolder holder;
		
		if (convertView == null)
		{
			
			holder = new ViewHolder();
			
			convertView = mInflater.inflate(R.layout.st_list_row, null);
			
			holder.st_list_row_container = (LinearLayout) convertView.findViewById(R.id.st_list_row_container);
			holder.textview_List = new ArrayList<TextView>();
			
			for(int i = 0; i < header_count; i++)
			{
				
				TextView textview = null;
				
				if(i == 0)
					textview = makeTextView(mContext, Gravity.CENTER);
				else
					textview = makeTextView(mContext, Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				
				holder.textview_List.add(textview);

			}
			
			convertView.setTag(holder);

		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.st_list_row_container.removeAllViews();

		GSMonthInOutDetail detail = item_list.get(position);

		holder.textview_List.get(0).setText(detail.day);
		holder.st_list_row_container.addView(holder.textview_List.get(0));

		String[] values = null;

		if (this.requestType == AppConfig.STATE_AMOUNT)
			values = detail.getStringValues(AppConfig.STATE_AMOUNT);
		else if (this.requestType == AppConfig.STATE_PRICE)
			values = detail.getStringValues(AppConfig.STATE_PRICE);

		for(int i = 1; i < holder.textview_List.size(); i++)
		{
			holder.textview_List.get(i).setText(values[i]);
			holder.st_list_row_container.addView(holder.textview_List.get(i));
		}
		
		return convertView;

	}
	
	class ViewHolder
	{
		LinearLayout st_list_row_container;
		ArrayList<TextView> textview_List;
	}
	
	private TextView makeTextView(Context context, int gravity)
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
		
		return tv;

	}

}

package com.geurimsoft.bokangnew.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.data.StatsTotalData;

public class StatsTotalView {
	
	private LinearLayout stock_layout, release_yi_layout, release_gj_layout, petosa_layout;
	
	private Context mContext;
	
	private StatsTotalData statsTotalData;
	
	private int stock_header_count;
	private int release_yi_header_count;
	private int release_gj_header_count;
	private int petosa_header_count;
	
	private String[] stock_header_titles;
	private String[] release_yi_header_titles;
	private String[] release_gj_header_titles;
	private String[] petosa_header_titles;
	
	private String[] stock_total_items;
	private String[] petosa_total_items;
	
	private int stock_count;
	private int release_yi_count;
	private int release_gj_count;
	private int petosa_count;
	
	private ArrayList<String[]> stock_list;
	private ArrayList<String[]> release_yi_list;
	private ArrayList<String[]> release_gj_list;
	private ArrayList<String[]> petosa_list;
	
	private String stock_total;
	private String release_total;
	private String petosa_total;
	
	public StatsTotalView(Context _context, StatsTotalData _statsTotalData) {
		this.mContext = _context;
		this.statsTotalData = _statsTotalData;
		
		this.stock_header_count = statsTotalData.getStock_header_count();
		this.release_yi_header_count = statsTotalData.getRelease_yi_header_count();
		this.release_gj_header_count = statsTotalData.getRelease_gj_header_count();
		this.petosa_header_count = statsTotalData.getPetosa_header_count();
		
		this.stock_header_titles = statsTotalData.getStock_header_titles();
		this.release_yi_header_titles = statsTotalData.getRelease_yi_header_titles();
		this.release_gj_header_titles = statsTotalData.getRelease_gj_header_titles();
		this.petosa_header_titles = statsTotalData.getPetosa_header();
		
		this.stock_count = statsTotalData.getStock_count();
		this.release_yi_count = statsTotalData.getRelease_yi_count();
		this.release_gj_count = statsTotalData.getRelease_gj_count();
		this.petosa_count = statsTotalData.getPetosa_count();
		
		this.stock_list = statsTotalData.getStock_list();
		this.release_yi_list = statsTotalData.getRelease_yi_list();
		this.release_gj_list = statsTotalData.getRelease_gj_list();
		this.petosa_list = statsTotalData.getPetosa_list();
		
		this.stock_total = statsTotalData.getStock_total();
		this.release_total = statsTotalData.getRelease_total();
		this.petosa_total = statsTotalData.getPetosa_total();
		
		this.stock_total_items =  statsTotalData.getStock_total_items();
		this.petosa_total_items = statsTotalData.getPetosa_total_items();
	}
	
	public void makeStockStatsView(LinearLayout _stock_layout) {
		
		stock_layout = _stock_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		//Stock Header Layout
		for(int stock_header_index = 0; stock_header_index < this.stock_header_count; stock_header_index++) {
			
			TextView stock_title_textview = makeMenuTextView(mContext, stock_header_titles[stock_header_index], "#ffffff", Gravity.CENTER);
			
			header_layout.addView(stock_title_textview);
		}
		
		stock_layout.addView(header_layout);
		
		if(stock_list == null || stock_list.size() <= 0)
			return;
		
		TextView stock_item_textview;
		
		for(int stock_index = 0; stock_index < this.stock_list.size(); stock_index++) {
			
			String[] stock_items = stock_list.get(stock_index);
			
			LinearLayout stock_row_layout = new LinearLayout(mContext);
			
			stock_row_layout.setLayoutParams(params);
			stock_row_layout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(int i = 0; i < stock_items.length; i++) {
				
				int gravity = 0;
				if(i == 0) {
					
					gravity = Gravity.CENTER;
					
					if(stock_index == stock_count - 1)
						stock_item_textview = makeMenuTextView(mContext, stock_items[i], "#000000", gravity);
					else
						stock_item_textview = makeRowTextView(mContext, stock_items[i], gravity);
						
					
					stock_row_layout.addView(stock_item_textview);
				} else {
					
					gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
					
					if(stock_index == stock_count - 1)
						stock_item_textview = makeMenuTextView(mContext, stock_items[i], "#000000", gravity);
					else
						stock_item_textview = makeRowTextView(mContext, stock_items[i], gravity);
					
					stock_row_layout.addView(stock_item_textview);
				}
				
			}
			stock_layout.addView(stock_row_layout);
		}
		
		LinearLayout total_layout = new LinearLayout(mContext);
		total_layout.setLayoutParams(params);
		total_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView total_textview = null;
		
		for(int i = 0; i < stock_total_items.length; i++) {
			
			int gravity = 0;
			if(i == 0) {
				gravity = Gravity.CENTER;
				
			} else {
				
				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
			}
			total_textview = makeMenuTextView(mContext, stock_total_items[i], "#000000", gravity);
			
			total_layout.addView(total_textview);
		}
		
		stock_layout.addView(total_layout);
		
	}
	
	public void makeReleaseYonginStatsView(LinearLayout _release_layout) {
		
		release_yi_layout = _release_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		//Release Header Layout
		for(int release_header_index = 0; release_header_index < this.release_yi_header_count; release_header_index++) {
			
			TextView release_title_textview = makeMenuTextView(mContext, release_yi_header_titles[release_header_index], "#ffffff", Gravity.CENTER);
			
			header_layout.addView(release_title_textview);
		}
		
		release_yi_layout.addView(header_layout);
		
		if(release_yi_list == null || release_yi_list.size() <= 0)
			return;
		
		TextView release_item_textview;
		
		for(int release_index = 0; release_index < this.release_yi_list.size(); release_index++) {
			
			String[] release_items = release_yi_list.get(release_index);
			
			LinearLayout release_row_layout = new LinearLayout(mContext);
			
			release_row_layout.setLayoutParams(params);
			release_row_layout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(int i = 0; i < release_items.length; i++) {
				
				int gravity = 0;
					
				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
					
				release_item_textview = makeRowTextView(mContext, release_items[i], gravity);
					
				release_row_layout.addView(release_item_textview);
			}
			release_yi_layout.addView(release_row_layout);
		}
		
	}

	public void makeReleaseGwangjuStatsView(LinearLayout _release_layout) {
		
		release_gj_layout = _release_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		//Release Header Layout
		for(int release_header_index = 0; release_header_index < this.release_gj_header_count; release_header_index++) {
			
			TextView release_title_textview = makeMenuTextView(mContext, release_gj_header_titles[release_header_index], "#ffffff", Gravity.CENTER);
			
			header_layout.addView(release_title_textview);
		}
		
		release_gj_layout.addView(header_layout);
		
		if(release_gj_list == null || release_gj_list.size() <= 0)
			return;
		
		TextView release_item_textview;
		
		for(int release_index = 0; release_index < this.release_gj_list.size(); release_index++) {
			
			String[] release_items = release_gj_list.get(release_index);
			
			LinearLayout release_row_layout = new LinearLayout(mContext);
			
			release_row_layout.setLayoutParams(params);
			release_row_layout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(int i = 0; i < release_items.length; i++) {
				
				int gravity = 0;
					
				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
					
				release_item_textview = makeRowTextView(mContext, release_items[i], gravity);
					
				release_row_layout.addView(release_item_textview);
			}
			release_gj_layout.addView(release_row_layout);
		}
		
	}
	
	public void makePetosaStatsView(LinearLayout _petosa_layout) {
		
		petosa_layout = _petosa_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		//petosa Header Layout
		for(int petosa_header_index = 0; petosa_header_index < this.petosa_header_count; petosa_header_index++) {
			
			TextView petosa_title_textview = makeMenuTextView(mContext, petosa_header_titles[petosa_header_index], "#ffffff", Gravity.CENTER);
			
			header_layout.addView(petosa_title_textview);
		}
		
		petosa_layout.addView(header_layout);
		
		if(petosa_list == null || petosa_list.size() <= 0)
			return;
		
		TextView petosa_item_textview;
		
		for(int petosa_index = 0; petosa_index < this.petosa_list.size(); petosa_index++) {
			
			String[] petosa_items = petosa_list.get(petosa_index);
			
			LinearLayout petosa_row_layout = new LinearLayout(mContext);
			
			petosa_row_layout.setLayoutParams(params);
			petosa_row_layout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(int i = 0; i < petosa_items.length; i++) {
				
				int gravity = 0;
				if(i == 0) {
					
					gravity = Gravity.CENTER;
					
					petosa_item_textview = makeRowTextView(mContext, petosa_items[i], gravity);
						
					petosa_row_layout.addView(petosa_item_textview);
				} else {
					
					gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
					
					petosa_item_textview = makeRowTextView(mContext, petosa_items[i], gravity);
					
					petosa_row_layout.addView(petosa_item_textview);
				}
				
			}
			petosa_layout.addView(petosa_row_layout);
		}
		
		LinearLayout total_layout = new LinearLayout(mContext);
		total_layout.setLayoutParams(params);
		total_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView total_textview = null;
		
		for(int i = 0; i < petosa_total_items.length; i++) {
			
			int gravity = 0;
			if(i == 0) {
				gravity = Gravity.CENTER;
				
			} else {
				
				gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
			}
			total_textview = makeMenuTextView(mContext, petosa_total_items[i], "#000000", gravity);
			
			total_layout.addView(total_textview);
		}
		
		petosa_layout.addView(total_layout);
		
	}
	
	private TextView makeMenuTextView(Context context, String str, String color, int gravity) {
		
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
	
	
	private TextView makeRowTextView(Context context, String str, int gravity) {
		
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

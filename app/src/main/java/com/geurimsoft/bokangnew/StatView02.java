package com.geurimsoft.bokangnew;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.geurimsoft.chart.GChart01;
import com.geurimsoft.chart.Result;
import com.geurimsoft.conf.AppConfig;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatView02 extends Activity 
{
	
	/*
	 * 조건에 해당하는 UI
	 * */
	View mPickDate,mpickTime;
	Button button_enter, home_bt, search_bt, cctv_bt, setting_bt,timechoice1,timechoice2,redbt_01,bluebt_02;
	TextView timetext1, timetext2;
	LinearLayout gonelayout; 
	
	/*
	 * 화면 전환에 관한 UI
	 * */
	Button btnRedLayout, btnBlueLayout, btnGreenLayout;
	LinearLayout redLayout, blueLayout, greenLayout;
	
	int mYear, mMonth, mDay, dYear, dMonth, dDay;
	
	ArrayList <String>  arrCustomerList = new ArrayList<String>();//거래처별 선택 
	ArrayList <String>  arrGoodsList = new ArrayList<String>();//거래처별 선택 
	ArrayList <String>  arrCarList = new ArrayList<String>();//거래처별 선택 
	
	
	/*
	 * 차트와 관련된 뷰 
	 * */
    protected GraphicalView mChartView;

    @Override
	protected void onCreate(Bundle savedInstanceState) 
    {
	
    	super.onCreate(savedInstanceState);
    	AppConfig.activities.add(StatView02.this);
		setContentView(R.layout.statviewlayout);
	
		setUserInterface();
		
	}
    
    private List<Result> getResults() 
    {
    
    	List<Result> results = new ArrayList<Result>();
        results.add(new Result(new Date(108, 9, 1), 8.8));
        results.add(new Result(new Date(108, 9, 8), 9.0));
        results.add(new Result(new Date(108, 9, 15), 10.0));
        results.add(new Result(new Date(108, 9, 22), 9.5));
        
        return results;
        
    }

  
	/*
	 * 화면을 구성요소를 정함 
	 */
	public void setUserInterface()
	{
		
		//화면 전환 
		redLayout 	= (LinearLayout)this.findViewById(R.id.redlayout);
		blueLayout 	= (LinearLayout)this.findViewById(R.id.bluelayout);
		greenLayout	= (LinearLayout)this.findViewById(R.id.greenlayout);
		
		redLayout.setVisibility(View.VISIBLE);
		blueLayout.setVisibility(View.GONE);
		greenLayout.setVisibility(View.GONE);
		
		btnRedLayout = (Button)this.findViewById(R.id.btnRedLayout);
		btnBlueLayout= (Button)this.findViewById(R.id.btnBlueLayout);
		btnGreenLayout= (Button)this.findViewById(R.id.btnGreenLayout);
				
		btnRedLayout.setOnClickListener(clicklistener);
		btnBlueLayout.setOnClickListener(clicklistener);
		btnGreenLayout.setOnClickListener(clicklistener);
		
        button_enter = (Button) findViewById(R.id.button_enter);
        button_enter.setOnClickListener(clicklistener);
        
        home_bt = (Button) findViewById(R.id.home_bt);
        home_bt.setOnClickListener(clicklistener);
        search_bt = (Button) findViewById(R.id.search_bt);
        search_bt.setOnClickListener(clicklistener);
        cctv_bt = (Button) findViewById(R.id.cctv_bt);
        cctv_bt.setOnClickListener(clicklistener);
        setting_bt = (Button) findViewById(R.id.setting_bt);
        setting_bt.setOnClickListener(clicklistener);
        
 
	}
	
	private void UpdateNow() 
	{
		timetext1.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
		timetext2.setText(String.format("%d/%d", dYear, dMonth ));
	}
	
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() 
	{
	
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		{
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			UpdateNow();
		}
		
	};
	
	DatePickerDialog.OnDateSetListener dDateSetListener = new DatePickerDialog.OnDateSetListener() 
	{
	
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		{
			dYear = year;
			dMonth = monthOfYear;
			dDay = dayOfMonth;
			UpdateNow();
		}
		
	};
	
	protected DatePickerDialog datePickerDialog;
	
	public void changeStatView(int layoutid)
	{
		
		redLayout.setVisibility(View.GONE);
		blueLayout.setVisibility(View.GONE);
		greenLayout.setVisibility(View.GONE);
			
		if(layoutid == R.id.btnRedLayout)	// 목록 선택
		{  
			redLayout.setVisibility(View.VISIBLE);			
		}
		else if(layoutid == R.id.btnBlueLayout)	// 차트 1
		{
			blueLayout.setVisibility(View.VISIBLE);
			viewChart01();		
		}
		else if(layoutid == R.id.btnGreenLayout)	// 차트 1
		{
			greenLayout.setVisibility(View.VISIBLE);
		}
		
		//통계관련된 작업을 수행함 
		
	}
	
	public void viewChart01()
	{
	
		GChart01 chart = new GChart01();
 
		mChartView = chart.getView(this, getResults());
		this.blueLayout.removeAllViews();
		this.blueLayout.addView(mChartView);
		
	}
	
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle,
			double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) 
	{
    
		renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(4000);
        renderer.setYAxisMax(4600);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        
	}
    
	protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) 
	{
    
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        int length = colors.length;

        for (int i = 0; i < length; i++) 
        {
        
        	SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
            
        }
        
        return renderer;
        
	}
 	
	View.OnClickListener clicklistener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v) 
		{
		
			if(v.getId() == R.id.button_enter)
			{
			
				LinearLayout LinearLayout = (LinearLayout)findViewById(R.id.gonelayout);
				
				if(LinearLayout.getVisibility()==View.VISIBLE)
				{
					LinearLayout.setVisibility(View.GONE);
				}
				else if(LinearLayout.getVisibility()==View.GONE)
				{
					LinearLayout.setVisibility(View.VISIBLE);
				}
				
			}
			else if(v.getId() == R.id.search_bt)
			{		
			}
			else if(v.getId() == R.id.btnRedLayout)		// 목록 선택
			{  
				changeStatView(v.getId());
			}
			else if(v.getId() == R.id.btnBlueLayout)	// 차트 1
			{
				changeStatView(v.getId());
			}
			else if(v.getId() == R.id.btnGreenLayout)	// 차트 1
			{
				changeStatView(v.getId());
			}
			else if(v.getId() == R.id.setting_bt)
			{
			}
			
		 }
		
	};
}

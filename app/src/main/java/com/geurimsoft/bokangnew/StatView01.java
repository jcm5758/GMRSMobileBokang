package com.geurimsoft.bokangnew;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.Customer;
import com.geurimsoft.data.DownloadInterface;
import com.geurimsoft.data.Product;
import com.geurimsoft.data.SearchResult;
import com.geurimsoft.data.Vehicle;
import com.geurimsoft.util.AppFunc;
import com.geurimsoft.util.ItemXmlParser;
 

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StatView01 extends Activity implements DownloadInterface
{
	
	/*
	 * 조건에 해당하는 UI
	 * */
	View mPickDate,mpickTime;
	Button button_enter, home_bt, search_bt, cctv_bt, setting_bt,timechoice1,timechoice2,redbt_01,bluebt_02;
	TextView timetext1, timetext2;
	LinearLayout gonelayout; 
	
	TextView txtStartDate, txtEndDate;
	TextView txtSelMonth;
	
	Spinner spinner1, spinner2, spinner3;
	
	Button btnSearch;
	
	/*
	 * 화면 전환에 관한 UI
	 * */
	Button btnRedLayout, btnBlueLayout, btnGreenLayout;
	LinearLayout redLayout, blueLayout, greenLayout;
	
	int mYear, mMonth, mDay, dYear, dMonth, dDay;
	int mStartYear, mStartMonth, mStartDay;
	int mEndYear, mEndMonth, mEndDay;
	
	int mStartYear2,mStartMonth2;
	int mEndYear2, mEndMonth2;
	
	private TextView currTextView = null;
	protected DatePickerDialog datePickerDialog;
	
	private ListView lvListResult;
	
	private boolean isSelDayType= true ; //true- day, false- month;

	/*
	 * 차트와 관련된 뷰 
	 * */
    protected GraphicalView mChartView;
    private static final int SERIES_NR = 2;
    private String[] mMenuText;
    private String[] mMenuSummary;
	
	ArrayList <Customer>  arrCustomerList = new ArrayList<Customer>();//거래처별 선택 
	ArrayList <Product>  arrGoodsList = new ArrayList<Product>();//거래처별 선택 
	ArrayList <Vehicle>  arrCarList = new ArrayList<Vehicle>();//거래처별 선택 
	
	/*
	 * 
	 * 결과 값 
	 * */
	private ArrayList<SearchResult> thiResultList = null;
	
    Context thisContext = null;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) 
    {
	
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.statviewlayout);
		
		AppConfig.activities.add(StatView01.this);
		
		thisContext = this;
		
		setUserInterface();
		initDate();
		UpdateList();
		
	}
    
	private void initDate()
	{

		Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		dYear = cal.get(Calendar.YEAR);
		dMonth = cal.get(Calendar.MONTH);
		dDay = cal.get(Calendar.DAY_OF_MONTH);
 		txtStartDate.setText(String.format("%d/%d/%d", mYear, 1, 1));
		txtEndDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
		
		String rstQuery =  makeSearchUrl();
		ItemXmlParser.getSearchResultList(thisContext, this, rstQuery, AppConfig.URL_TYPE_SEARCH);

	 	ItemXmlParser.getSearchResultList(thisContext, this, AppConfig.GET_CUSTOMER_LIST_URL, AppConfig.URL_TYPE_CUSTOMER);
	 	ItemXmlParser.getSearchResultList(thisContext, this, AppConfig.GET_PRODUCT_LIST_URL, AppConfig.URL_TYPE_PRODUCT);
	 	ItemXmlParser.getSearchResultList(thisContext, this, AppConfig.GET_VEHICLE_LIST_URL, AppConfig.URL_TYPE_VEHICLE);

	}
 
	public void UpdateList()
	{
	
		arrCustomerList.add(new Customer());
	    arrCustomerList.add(new Customer(1,"삼성"));
	    arrCustomerList.add(new Customer(2,"현대"));
 	  
	    ArrayAdapter<String> aaCustomer = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppFunc.getStringArrayFromCustomer(arrCustomerList));
	    this.spinner1.setAdapter(aaCustomer);
	    arrGoodsList.add(new Product());
   	
	    arrGoodsList.add(new Product(1,"50mm"));
	    arrGoodsList.add(new Product(1,"40mm"));
 
	    ArrayAdapter<String> aaGoods = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppFunc.getStringArrayFromProduct(arrGoodsList));
	    this.spinner2.setAdapter(aaGoods);
	    	
	    arrCarList.add(new Vehicle());
	    arrCarList.add(new Vehicle(1,"02구1234"));
	    arrCarList.add(new Vehicle(2,"10미2345"));
 
	    ArrayAdapter<String> aaCar  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppFunc.getStringArrayFromVehicle(arrCarList));
	    this.spinner3.setAdapter(aaCar);
	    
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
		
		spinner1= (Spinner)this.findViewById(R.id.spinner_1);
		spinner2= (Spinner)this.findViewById(R.id.spinner_2);
		spinner3= (Spinner)this.findViewById(R.id.spinner_3);
	
		spinner1.setOnItemSelectedListener(onItemSelectListener);
		spinner2.setOnItemSelectedListener(onItemSelectListener);
		spinner3.setOnItemSelectedListener(onItemSelectListener);
		
		btnSearch = (Button )this.findViewById(R.id.btnStatSearch);
		btnSearch.setOnClickListener(clicklistener);

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
        
        txtStartDate = (TextView)this.findViewById(R.id.txtStartDate);
        txtEndDate= (TextView)this.findViewById(R.id.txtEndDate);
        txtStartDate.setOnClickListener(clicklistener);
        txtEndDate.setOnClickListener(clicklistener);   
        
        txtSelMonth = (TextView)this.findViewById(R.id.txtSelMonth);
        txtSelMonth.setOnClickListener(clicklistener);        
        
        lvListResult = (ListView)this.findViewById(R.id.lvListResult);
              
	}
	
	private String makeSearchUrl()
	{
	
		String  sbBuffer = new String();
		
		int sIdx1 = this.spinner1.getSelectedItemPosition();
		int sIdx2 = this.spinner2.getSelectedItemPosition();
		int sIdx3 = this.spinner3.getSelectedItemPosition();
				
		String selStartDate = (String)txtStartDate.getText();
		 
		String []arrStartDate= selStartDate.split("/");
		selStartDate = arrStartDate[0];
		
		if(arrStartDate[1].length() < 2)
		{
			selStartDate = selStartDate + "0"+arrStartDate[1];
		}
		else
		{
			selStartDate = selStartDate + arrStartDate[1];
		}
		
		if(arrStartDate[2].length() < 2)
		{
			selStartDate = selStartDate + "0"+arrStartDate[2];
		}
		else
		{
			selStartDate = selStartDate + arrStartDate[2];
		}
		
		Toast.makeText(getApplicationContext(), "start-"+selStartDate, Toast.LENGTH_LONG).show();
		
		String selEndDate = (String)txtEndDate.getText();
 		
		String []arrEndDate= selEndDate.split("/");
		selEndDate = arrEndDate[0];

		if(arrEndDate[1].length() < 2)
		{
			selEndDate = selEndDate + "0"+arrEndDate[1];
		}
		else
		{
			selEndDate = selEndDate + arrEndDate[1];
		}
		
		if(arrEndDate[2].length() < 2)
		{
			selEndDate = selEndDate + "0"+arrEndDate[2];
		}
		else
		{
			selEndDate = selEndDate + arrEndDate[2];
		}
				
		if( sIdx1 > 0 )
		{
		
			Customer thisCustomer = arrCustomerList.get(sIdx1);
			String selItem =thisCustomer.getId()+"";
			String selQuery= selStartDate+","+selEndDate+","+selItem;
			sbBuffer = AppConfig.SEARCH_URL_2_1+selQuery+AppConfig.SEARCH_URL_2_2;
 		
		}
		else if( sIdx2 >0 )
		{
 			
			Product thisProduct = arrGoodsList.get(sIdx1);
			String selItem =thisProduct.getId()+"";
 			String selQuery= selStartDate+","+selEndDate+","+selItem;
			sbBuffer = AppConfig.SEARCH_URL_3_1+selQuery+AppConfig.SEARCH_URL_3_2;
 			
		}
		else if( sIdx3 > 0 )
		{
			
			Vehicle thisVehicle = arrCarList.get(sIdx1);
			String selItem =thisVehicle.getId()+"";
 		
 			String selQuery= selStartDate+","+selEndDate+","+selItem;
			sbBuffer = AppConfig.SEARCH_URL_4_1+selQuery+AppConfig.SEARCH_URL_4_2;
 			
		}
		else
		{
		
			String selQuery= selStartDate+","+selEndDate;
			sbBuffer = AppConfig.SEARCH_URL_1_1+selQuery+AppConfig.SEARCH_URL_1_2;
 			
		}
		
		Toast.makeText(getApplicationContext(), "query-"+sbBuffer, Toast.LENGTH_LONG).show();
		
		Log.d("QUERY",sbBuffer);
	
		return sbBuffer;
		
	}
	
	public void searchResult()
	{
		String rstQuery =  makeSearchUrl();
		ItemXmlParser.getSearchResultList(thisContext, this, rstQuery, AppConfig.URL_TYPE_SEARCH);
	}
	
	private void UpdateNow() 
	{		
		if(currTextView != null)
		{
			currTextView.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
		}
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
	
	public void changeStatView(int layoutid)
	{
		
		redLayout.setVisibility(View.GONE);
		blueLayout.setVisibility(View.GONE);
		greenLayout.setVisibility(View.GONE);
		
		
		if(layoutid == R.id.btnRedLayout){ // 목록 선택 
			redLayout.setVisibility(View.VISIBLE);
			
		}else if(layoutid == R.id.btnBlueLayout){// 차트 1
			blueLayout.setVisibility(View.VISIBLE);
			viewChart01();
		
		}else if(layoutid == R.id.btnGreenLayout){// 차트 1
			greenLayout.setVisibility(View.VISIBLE);
			viewChart02();

		}
		
		//통계관련된 작업을 수행함 
	}
	
	public void viewChart01()
	{	
		mChartView =ChartFactory.getLineChartView(this, getDemoDataset2(), getDemoRenderer());
		this.blueLayout.removeAllViews();
		this.blueLayout.addView(mChartView);
	}
	
	public void viewChart02()
	{
		XYMultipleSeriesRenderer renderer = getBarDemoRenderer();
	    setChartSettings(renderer);
	    mChartView = ChartFactory.getBarChartView(this, getBarDemoDataset(), renderer, Type.DEFAULT);
	    this.greenLayout.removeAllViews();
		this.greenLayout.addView(mChartView);
 	}
 
	OnItemSelectedListener onItemSelectListener = new  OnItemSelectedListener()
	{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
		{
 		
			if(arg0 == spinner1 )
			{
 				spinner2.setSelection(0);
				spinner3.setSelection(0);
			}
			
			if(arg0 == spinner2 && arg2 >0)
			{ 
				spinner1.setSelection(0);
				spinner3.setSelection(0);
			}
			
			if(arg0 == spinner3 && arg2 >0)
			{
				spinner1.setSelection(0);
				spinner2.setSelection(0);
			}
			
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
		
	};
	
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
			else if(v.getId() == R.id.txtStartDate)
			{
				currTextView = (TextView) findViewById(R.id.txtStartDate);
				isSelDayType = true;
				Dialog datepicker = new DatePickerDialog(StatView01.this, mDateSetListener, mYear, mMonth, mDay);				
				datepicker.show();
			}
			else if(v.getId() == R.id.txtEndDate)
			{
				currTextView = (TextView) findViewById(R.id.txtEndDate);
				isSelDayType = true;
				Dialog datepicker = new DatePickerDialog(StatView01.this, mDateSetListener, mYear, mMonth, mDay);				
				datepicker.show();
			}
			else if(v.getId() == R.id.txtSelMonth)
			{
				currTextView = (TextView) findViewById(R.id.txtSelMonth);
				isSelDayType = false;
				Dialog datepicker = new DatePickerDialog(StatView01.this,  mDateSetListener,dYear, dMonth, 0);
				datepicker.show();
			}
			else if(v.getId() == R.id.btnStatSearch)
			{
				searchResult();
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
				Intent intent = new Intent(StatView01.this, AppSetting.class);
				startActivity(intent);
			}
			else if(v.getId() == R.id.search_bt)
			{
				LinearLayout searchLayout = (LinearLayout)findViewById(R.id.gonelayout);
				if(searchLayout.getVisibility()==View.VISIBLE)
				{
					searchLayout.setVisibility(View.GONE);
				}
				else if(searchLayout.getVisibility()==View.GONE)
				{
					searchLayout.setVisibility(View.VISIBLE);
				}
			}
			else if(v.getId() == R.id.home_bt)
			{
				Intent intent = new Intent(StatView01.this, AppMain.class);
				startActivity(intent);
			}
			else if(v.getId() == R.id.cctv_bt)
			{
				Intent intent = new Intent(StatView01.this, CCTVMain.class);
				startActivity(intent);
			}
			
		 }
		
	};
	   
	  /**
	   * XY CHart 
	   * @return
	   */
	private XYMultipleSeriesDataset getDemoDataset() 
	{
	
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    
		final int nr = 10;
	    Random r = new Random();
	    
	    for (int i = 0; i < SERIES_NR; i++) 
	    {
	    
	    	XYSeries series = new XYSeries("Demo series " + (i + 1));
	    	for (int k = 0; k < nr; k++) 
	    	{
	    		series.add(k, 20 + r.nextInt() % 100);
	    	}
	    	dataset.addSeries(series);
	    
	    }
	    
	    return dataset;
	    
	  }
	
	private XYMultipleSeriesDataset getDemoDataset2() 
	{
	    
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    final int nr = 10;
  	    XYSeries series = new XYSeries(" 통계 ");
	    
  	    if(thiResultList != null )
  	    {
		
  	    	for (int k = 0; k < thiResultList.size(); k++) 
  	    	{
		    	
		    	SearchResult thisSearchResult = thiResultList.get(k);
		    	Log.d("data","i=,k="+k+",cc-"+ thisSearchResult.getUnitD());
		    	series.add(thisSearchResult.getIdI(),  thisSearchResult.getUnitD());
		    	series.addAnnotation(thisSearchResult.getServiceDate(), thisSearchResult.getIdI(),  thisSearchResult.getUnitD()); 
		    
  	    	}
		    
  	    	dataset.addSeries(series);
	    }
  	    
 	    return dataset;
 	    
	  }
	
	  private XYMultipleSeriesRenderer getDemoRenderer() 
	  {
		
		  XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		  renderer.setAxisTitleTextSize(16);
		  renderer.setChartTitleTextSize(20);
		  renderer.setLabelsTextSize(15);
		  renderer.setLegendTextSize(15);
		  renderer.setPointSize(5f);
		  renderer.setYAxisMax(20.0);// 향후 값을 계산하는 것으로 변경해야 함 
		  renderer.setYAxisMin(0.0);// 향후 값을 계산하는 것으로 변경해야 함 

		  renderer.setMargins(new int[] {20, 30, 15, 0});
		  XYSeriesRenderer r = new XYSeriesRenderer();
		  r = new XYSeriesRenderer();
		  r.setPointStyle(PointStyle.CIRCLE);
		  r.setColor(Color.GREEN);
		  r.setFillPoints(true);
		  renderer.addSeriesRenderer(r);
		  renderer.setAxesColor(Color.DKGRAY);
		  renderer.setLabelsColor(Color.LTGRAY);
		  
		  return renderer;
		  
	  }
	  
	  private XYMultipleSeriesDataset getBarDemoDataset() 
	  {
	  
		  XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		  final int nr = 10;
		  Random r = new Random();
		  
		  if(thiResultList != null )
		  {
			  
			  CategorySeries series = new CategorySeries("통계" );
	      
			  for (int k = 0; k < thiResultList.size(); k++) 
			  {
		    	
				  SearchResult thisSearchResult = thiResultList.get(k);
				  Log.d("data","i=,k="+k+",cc-"+ thisSearchResult.getUnitD());
				  
				  series.add(  thisSearchResult.getUnitD());			  
			  }
		    
			  dataset.addSeries(series.toXYSeries());
	    
		  }
	    
		  return dataset;
		  
	  }

	  public XYMultipleSeriesRenderer getBarDemoRenderer() 
	  {
	  
		  XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		  renderer.setAxisTitleTextSize(16);
		  renderer.setChartTitleTextSize(20);
		  renderer.setLabelsTextSize(15);
		  renderer.setLegendTextSize(15);
		  renderer.setYAxisMax(20.0);// 향후 값을 계산하는 것으로 변경해야 함 
		  renderer.setMargins(new int[] {20, 30, 15, 0});
		  SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		  r.setColor(Color.BLUE);
		  renderer.addSeriesRenderer(r);

		  return renderer;
	  
	  }

	  private void setChartSettings(XYMultipleSeriesRenderer renderer) 
	  {
	  
		  renderer.setChartTitle("통계 ");
		  renderer.setXTitle("x values");
		  renderer.setYTitle("y values");
		  renderer.setXAxisMin(0.5);
		  renderer.setXAxisMax(10.5);
		  renderer.setYAxisMin(0);
		  renderer.setYAxisMax(20);
	  
	  }
	  	 
	  @Override
	  public void downloadComplete(boolean result, String resultMesg,	ArrayList resultList, int contentType) 
	  {
		
		  if(contentType == AppConfig.URL_TYPE_SEARCH)
		  {
			
			  Toast.makeText(thisContext, "COUNT-"+resultList.size(), Toast.LENGTH_LONG).show();
			  thiResultList = resultList;

			  ResultListAdapter jAdapter = new ResultListAdapter(thisContext, R.layout.statlistitem, resultList);
			  lvListResult.setAdapter(jAdapter);
	 		
			  viewChart01();
			  viewChart02();
		
		  }
		  else if(contentType == AppConfig.URL_TYPE_CUSTOMER)
		  {			
			  ArrayAdapter<String> aaCustomer = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppFunc.getStringArrayFromCustomer(resultList));
			  this.spinner1.setAdapter(aaCustomer);
		  }
		  else if(contentType == AppConfig.URL_TYPE_PRODUCT)
		  {
			  ArrayAdapter<String> aaGoods = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppFunc.getStringArrayFromProduct(resultList));
			  this.spinner2.setAdapter(aaGoods);
		  }
		  else if(contentType == AppConfig.URL_TYPE_VEHICLE)
		  {
			  ArrayAdapter<String> aaCar  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppFunc.getStringArrayFromVehicle(resultList));
			  this.spinner3.setAdapter(aaCar);	    
		  }
	
	  }
	
	  private class ResultListAdapter extends BaseAdapter
	  {
		
		  Context listContext;
		  private LayoutInflater mInflater;
		  ArrayList<SearchResult> searhResultList;
		  int layout;
		
		  public ResultListAdapter(Context context, int aLayout, ArrayList<SearchResult> pItemList)
		  {			
			  listContext = context;
			  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  searhResultList = pItemList;
			  layout = aLayout;
		  } 
		  
		  @Override
		public int getCount() { return searhResultList.size(); } 
		  @Override
		public Object getItem(int position) { return searhResultList.get(position); }
		  @Override
		public long getItemId(int position){ return position; }
		  
		  @Override
		public View getView(final int position, View convertView, ViewGroup parent)
		  {
			
			  final int pos = position;
			  final SearchResult thisItem = searhResultList.get(pos);
        	
			  if(convertView == null)
			  {
				  convertView = mInflater.inflate(layout, parent,false);
			  }
			
			  TextView tvListId = (TextView)convertView.findViewById(R.id.tvListId);
			  TextView tvListCustomer  = (TextView)convertView.findViewById(R.id.tvListCustomer);
			  TextView tvProduct  = (TextView)convertView.findViewById(R.id.tvProduct);
			  TextView tvVehicleNum  = (TextView)convertView.findViewById(R.id.tvVehicleNum);
			  TextView tvServiceDate  = (TextView)convertView.findViewById(R.id.tvServiceDate);
			  TextView tvUnit  = (TextView)convertView.findViewById(R.id.tvUnit);
			
			  tvListId.setText(thisItem.getId());
			  tvListCustomer.setText(thisItem.getCustomer());
			  tvProduct.setText(thisItem.getProduct());
			  tvVehicleNum.setText(thisItem.getVehicleNum());
			  tvServiceDate.setText(thisItem.getServiceDate());
			  tvUnit.setText(thisItem.getUnit());
 
			  return convertView;
		  
		  }
	
	  }
	  
}

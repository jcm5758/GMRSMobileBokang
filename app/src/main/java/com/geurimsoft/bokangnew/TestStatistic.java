package com.geurimsoft.bokangnew;

import android.app.Activity;
import android.os.Bundle;

public class TestStatistic extends Activity 
{

	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
    
		super.onCreate(savedInstanceState);
        setContentView(R.layout.test_statistic);
 
        openChart();
 
    }
 
    private void openChart()
    {    	
    	
    	/*GSDataStatisticYear sy = new GSDataStatisticYear(this, 2014);
    	XYMultipleSeriesRenderer multiRenderer = sy.getRenderer();
    	XYMultipleSeriesDataset dataset = sy.getDataSet();

    	LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
    	chartContainer.removeAllViews();
    	View mChart = ChartFactory.getBarChartView(TestStatistic.this, dataset, multiRenderer,Type.DEFAULT);
    	chartContainer.addView(mChart);*/
    	
    }
 
}

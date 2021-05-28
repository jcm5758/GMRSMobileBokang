package com.geurimsoft.grms.data;

import java.util.ArrayList;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.Log;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.StatsListData;

public class GSDataStatisticMonthChart
{
	
	private Context mContext;
	private String mDate;
	private GSMonthInOut data;

	private XYMultipleSeriesRenderer multiRenderer;
	private XYMultipleSeriesDataset dataset;

	public GSDataStatisticMonthChart(Context context, String _date, GSMonthInOut data)
	{

		this.mContext = context;
		this.mDate = _date;
		this.data = data;
		
		this.makeChart();

	}

	public XYMultipleSeriesRenderer getRenderer() {
		return this.multiRenderer;
	}
	public XYMultipleSeriesDataset getDataSet() {
		return this.dataset;
	}

	private void makeChart()
	{

		try
		{

			double[] Max = this.data.getMonthChartData();
			double Maxnode = Max[0];

			for(int i = 1; i < Max.length; i++)
			{
				if(Max[i] > Maxnode)
					Maxnode = Max[i];
			}

			int itemCount = this.data.recordCount - 1;
			dataset = new XYMultipleSeriesDataset();

			multiRenderer = new XYMultipleSeriesRenderer();
			multiRenderer.setXLabels(0);
			multiRenderer.setChartTitle(mDate);
			multiRenderer.setXTitle("일");
			multiRenderer.setYTitle("루베");

			int[] rainbow = mContext.getResources().getIntArray(R.array.rainbow);

			for(int i = 0; i < AppConfig.MODE_NAMES.length; i++)
			{

				XYSeries series = new XYSeries(AppConfig.MODE_NAMES[i]);

				for(int j = 0; j < itemCount; j++)
				{
					int itemIndex = i + j * (AppConfig.MODE_NAMES.length);
					series.add(j, Max[itemIndex]);
				}

				dataset.addSeries(series);

				// Creating XYSeriesRenderer to customize incomeSeries
				XYSeriesRenderer renderer = new XYSeriesRenderer();
				renderer.setColor(rainbow[i]);
				renderer.setPointStyle(PointStyle.CIRCLE);
				renderer.setLineWidth(2);
				renderer.setDisplayChartValues(true);
				renderer.setDisplayChartValuesDistance(10);

				multiRenderer.addSeriesRenderer(renderer);

			}

			multiRenderer.setChartTitleTextSize(40);
			multiRenderer.setAxisTitleTextSize(24);
			multiRenderer.setLabelsTextSize(15);
			multiRenderer.setZoomButtonsVisible(false);
			multiRenderer.setPanEnabled(false, false);
			multiRenderer.setClickEnabled(false);
			multiRenderer.setZoomEnabled(false, false);
			multiRenderer.setShowGridY(true);
			multiRenderer.setShowGridX(true);
			multiRenderer.setFitLegend(true);
			multiRenderer.setShowGrid(true);
			multiRenderer.setZoomEnabled(false);
			multiRenderer.setExternalZoomEnabled(false);
			multiRenderer.setAntialiasing(true);
			multiRenderer.setInScroll(true);
			multiRenderer.setLegendHeight(30);
			multiRenderer.setXLabelsAlign(Align.CENTER);
			multiRenderer.setYLabelsAlign(Align.LEFT);
			multiRenderer.setTextTypeface("sans_serif", Typeface.BOLD);
			multiRenderer.setLabelsColor(Color.BLACK);
			multiRenderer.setAxesColor(Color.BLACK);
			multiRenderer.setYLabels(10);
			multiRenderer.setYAxisMax(Maxnode+150);
			multiRenderer.setXAxisMin(-0.5);
			multiRenderer.setXAxisMax(itemCount);
			multiRenderer.setBarSpacing(0.5);
			multiRenderer.setBackgroundColor(Color.TRANSPARENT);
			multiRenderer.setMarginsColor(this.mContext.getResources().getColor(R.color.transparent_background));
			multiRenderer.setApplyBackgroundColor(true);
			multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

			for (int i = 0; i < itemCount; i++)
			{
				multiRenderer.addXTextLabel(i, (i + 1) + "");
			}

		}
		catch(Exception ex)
		{
			Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : makeChart() : " + ex.toString());
			return;
		}

	}

}

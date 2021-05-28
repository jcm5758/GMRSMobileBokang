package com.geurimsoft.bokangnew.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.conf.AppConfig;


public class MonthDatePickerDialog extends Dialog implements OnClickListener{

	private MonthWheelDatePicker MonthWheelDatePicker;
	private int currentYear;
	private int currentMonth;
	
	private Button confirm_button;
	
	private DialogListner dialogListner;
	
	private int selectYear;
	private int selectMonth;
	private int maxYear;
	
	public MonthDatePickerDialog(Context context, int _currentYear, int _maxYear, int _currentMonth, DialogListner _dialogListner) {
		super(context);
		// TODO Auto-generated constructor stub
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.month_datepicker_dialog);
		
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		LayoutParams params = getWindow().getAttributes();
	    params.width = LayoutParams.MATCH_PARENT;
	    params.height = LayoutParams.WRAP_CONTENT;
	    
	    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	    
		this.currentYear = _currentYear;
		this.currentMonth = _currentMonth;
		
		this.dialogListner = _dialogListner;
		this.maxYear = _maxYear;
		
		setInterface();
		
	}
	
	private void setInterface() {
		this.confirm_button = (Button)findViewById(R.id.confirm_button);
		this.confirm_button.setOnClickListener(this);
		
		this.MonthWheelDatePicker =  (MonthWheelDatePicker)findViewById(R.id.month_datepicker);
		
		this.MonthWheelDatePicker.setVisibleItems(8);
		this.MonthWheelDatePicker.setMinMaxYears(AppConfig.LIMIT_YEAR, maxYear);
		
		this.MonthWheelDatePicker.setYear(currentYear);
		this.MonthWheelDatePicker.setMonth(currentMonth);
		
		this.selectYear = currentYear;
		this.selectMonth = currentMonth;
		
		this.MonthWheelDatePicker.addDateChangedListener(new MonthWheelDatePicker.IDateChangedListener() {

			@Override
			public void onChanged(MonthWheelDatePicker sender, int oldYear, int year, int oldMonth, int newMonth) {
				// TODO Auto-generated method stub
				selectYear = year;
				selectMonth = newMonth;
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.confirm_button :
			dialogListner.OnConfirmButton(MonthDatePickerDialog.this, selectYear, selectMonth);
			break;
//			
		}
	}
	
	public interface DialogListner {
		public void OnConfirmButton(Dialog dialog, int selectYear, int selectMonth);

	}
}

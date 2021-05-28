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


public class YearDatePickerDialog extends Dialog implements OnClickListener{

	private YearWheelDatePicker yearWheelDatePicker;
	private int currentYear;
	private Button confirm_button;
	
	private DialogListner dialogListner;
	
	private int selectYear;
	private int maxYear;
	
	public YearDatePickerDialog(Context context, int _currentYear, int _maxYear, DialogListner _dialogListner) {
		super(context);
		// TODO Auto-generated constructor stub
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.year_datepicker_dialog);
		
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		LayoutParams params = getWindow().getAttributes();
	    params.width = LayoutParams.MATCH_PARENT;
	    params.height = LayoutParams.WRAP_CONTENT;
	    
	    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	    
		this.currentYear = _currentYear;
		this.dialogListner = _dialogListner;
		this.maxYear = _maxYear;
		
		setInterface();
		
	}
	
	private void setInterface() {
		this.confirm_button = (Button)findViewById(R.id.confirm_button);
		this.confirm_button.setOnClickListener(this);
		
		this.yearWheelDatePicker =  (YearWheelDatePicker)findViewById(R.id.year_datepicker);
		
		this.yearWheelDatePicker.setVisibleItems(8);
		this.yearWheelDatePicker.setMinMaxYears(AppConfig.LIMIT_YEAR, maxYear);
		
		this.yearWheelDatePicker.setYear(currentYear);
		
		this.selectYear = currentYear;
		
		this.yearWheelDatePicker.addDateChangedListener(new YearWheelDatePicker.IDateChangedListener() {
			@Override
			public void onChanged(YearWheelDatePicker sender, int oldYear, int _year) {
				selectYear = _year;
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.confirm_button :
			dialogListner.OnConfirmButton(YearDatePickerDialog.this, selectYear);
			break;
//			
		}
	}
	
	public interface DialogListner {
		public void OnConfirmButton(Dialog dialog, int selectYear);

	}
}

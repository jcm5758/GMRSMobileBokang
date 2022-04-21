package com.geurimsoft.bokangnew.view.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class GSAutoCompleteTextView extends AutoCompleteTextView
{

    private Context context;

    public GSAutoCompleteTextView(Context context)
    {
        super(context);
        this.context = context;
    }

    public GSAutoCompleteTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
    }

    public GSAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    public boolean enoughToFilter()
    {
        return true;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {}

    @Override
    public void onFilterComplete(int count) {}

}

package com.geurimsoft.bokangnew.view;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.widget.OnWheelChangedListener;
import com.geurimsoft.widget.WheelView;
import com.geurimsoft.widget.adapter.YearNumericWheelAdapter;

/**
 * @author igor.kostromin
 * 16.05.13 9:03
 *
 * Custom datepicker based on wheel control.
 */
public class YearWheelDatePicker extends LinearLayout {

    public static final int DEFAULT_VISIBLE_ITEMS = 3;

    public static final int DEFAULT_MIN_YEAR = 1900;
    public static final int DEFAULT_MAX_YEAR = 2050;

    public static final int DEFAULT_YEAR = 2000;
    public static final int DEFAULT_DAY = 15;
    public static final int DEFAULT_MONTH = 7;

    // state
    private int minYear;
    private int maxYear;

    // wheel controls to select birth date
    private WheelView wheelYear;


    public static interface IDateChangedListener {
        void onChanged(YearWheelDatePicker sender, int oldYear, int year);
    }

    private List<IDateChangedListener> dateChangedListeners = new ArrayList<IDateChangedListener>(  );

    public void addDateChangedListener( IDateChangedListener listener) {
        if (null == listener) throw new IllegalArgumentException( "listener is null" );
        Assert.assertTrue( !dateChangedListeners.contains( listener ));
        dateChangedListeners.add( listener );
    }

    public void removeDateChangedListener( IDateChangedListener listener ) {
        if (null == listener ) throw new IllegalArgumentException( "listener is null" );
        Assert.assertTrue( dateChangedListeners.contains( listener ) );
        dateChangedListeners.remove( listener );
    }

    private void raiseDateChangedEvent(int oldYear, int year) {
        if (!dateChangedListeners.isEmpty()) {
            List<IDateChangedListener > copy = new ArrayList<IDateChangedListener>( dateChangedListeners );
            for ( IDateChangedListener listener : copy ) {
                listener.onChanged( this, oldYear, year );
            }

        }
    }

    public YearWheelDatePicker( Context context ) {
        super( context );
        init(context );
    }

    public YearWheelDatePicker( Context context, AttributeSet attrs ) {
        super( context, attrs );
        init(context );
    }

    public YearWheelDatePicker( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        init(context );
    }

    public int getMinYear() {
        return minYear;
    }

    /**
     * Use this method to set years interval that will be awailable.
     * @param minYear
     * @param maxYear
     */
    public void setMinMaxYears( int minYear, int maxYear ) {
        if (minYear < 0) throw new IllegalArgumentException( "minYear" );
        if (maxYear < 0) throw new IllegalArgumentException( "maxYear" );
        if (minYear > maxYear) throw new IllegalArgumentException( "minYear should be <= maxYear" );
        //
        if (this.minYear != minYear || this.maxYear != maxYear) {
            this.minYear = minYear;
            this.maxYear = maxYear;

            // reinit wheelYear
            int year = getYear();
            wheelYear.setViewAdapter( new YearNumericWheelAdapter( this.getContext(), minYear, maxYear ) );
            if (year >= minYear && year <= maxYear) {
                setYear( year );
            } else {
                setYear(minYear);
            }
        }
    }

    public int getMaxYear() {
        return maxYear;
    }

    /**
     * Returns selected year.
     * @return
     */
    public int getYear() {
        return wheelYear.getCurrentItem() + minYear;
    }

    public void setYear( int year ) {
        if (year < minYear || year > maxYear) throw new IllegalArgumentException( "year should be between minYear and maxYear" );
        
        wheelYear.setCurrentItem( year - minYear );
    }


    /**
     * Sets the desired count of visible items.
     * @param count
     */
    public void setVisibleItems( int count ) {
        wheelYear.setVisibleItems( count );

        // trigger remeasuring
        this.requestLayout();
    }

    /**
     * Set locale (needed for months wheel).
     * Supported locales: "en-US", "ru-RU".
     * @param locale for example new Locale("ru", "RU");
     */

    private void init( final Context ctx) {
        LayoutInflater inflater = ( LayoutInflater ) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        inflater.inflate( R.layout.wheel_year, this, true );

        minYear = DEFAULT_MIN_YEAR;
        maxYear = DEFAULT_MAX_YEAR;

        wheelYear = ( WheelView ) findViewById( R.id.wheelYear );
        wheelYear.setTag( "wheelYear" ); // to debug inside wheel control code, can be safely removed
        wheelYear.setViewAdapter( new YearNumericWheelAdapter( ctx, minYear, maxYear ) );
        wheelYear.setCurrentItem( DEFAULT_YEAR - minYear );
        wheelYear.setVisibleItems( DEFAULT_VISIBLE_ITEMS );
        wheelYear.addChangingListener( new OnWheelChangedListener() {
            @Override
			public void onChanged( WheelView wheel, int oldValue, int newValue ) {
                int oldYear = oldValue + minYear;
                int newYear = newValue + minYear;
                raiseDateChangedEvent(oldYear, newYear );
            }
        } );
    }

}

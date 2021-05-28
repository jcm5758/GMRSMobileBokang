package com.geurimsoft.bokangnew.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class MonthWheelDatePicker extends LinearLayout {

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
    private WheelView wheelMonth;

    public static interface IDateChangedListener {
        void onChanged(MonthWheelDatePicker sender, int oldYear, int year, int oldMonth, int newMonth);
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

    private void raiseDateChangedEvent(int oldYear, int year, int oldMonth, int newMonth) {
        if (!dateChangedListeners.isEmpty()) {
            List<IDateChangedListener > copy = new ArrayList<IDateChangedListener>( dateChangedListeners );
            for ( IDateChangedListener listener : copy ) {
                listener.onChanged( this, oldYear, year, oldMonth, newMonth );
            }

        }
    }

    public MonthWheelDatePicker( Context context ) {
        super( context );
        init(context );
    }

    public MonthWheelDatePicker( Context context, AttributeSet attrs ) {
        super( context, attrs );
        init(context );
    }

    public MonthWheelDatePicker( Context context, AttributeSet attrs, int defStyle ) {
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

    
    public int getMonth() {
        return wheelMonth.getCurrentItem() + 1;
    }

    public void setMonth( int month) {
        if (month < 1 || month > 12) throw new IllegalArgumentException( "month should be between 1 and 12" );
        wheelMonth.setCurrentItem( month - 1 );
    }
    
    /**
     * Sets the desired count of visible items.
     * @param count
     */
    public void setVisibleItems( int count ) {
        wheelYear.setVisibleItems( count );
        wheelMonth.setVisibleItems( count );
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
        inflater.inflate( R.layout.wheel_month, this, true );

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
                
                int month  = getMonth();
                
                raiseDateChangedEvent(oldYear, newYear, month, month);
            }
        } );
        
        wheelMonth = ( WheelView ) findViewById( R.id.wheelMonth );
        wheelMonth.setTag( "wheelMonth" );
        wheelMonth.setViewAdapter( new MonthsAdapter( ctx ) );
        wheelMonth.setCurrentItem( DEFAULT_MONTH - 1 );
        wheelMonth.setVisibleItems( DEFAULT_VISIBLE_ITEMS );
        wheelMonth.addChangingListener( new OnWheelChangedListener() {
            @Override
			public void onChanged( WheelView wheel, int oldValue, int newValue ) {
                //
                int year = wheelYear.getCurrentItem() + minYear;
                int oldMonth = oldValue + 1;
                int newMonth = newValue + 1;
                
                raiseDateChangedEvent(year, year, oldMonth, newMonth);
            }
        } );
    }

    
    
    public static class MonthsAdapter extends YearNumericWheelAdapter {

        private Locale locale;

        public MonthsAdapter( Context context) {
            this(context, new Locale( "ko" , "KO" ) );
        }

        public static boolean isLocaleSupported(Locale locale) {
            return  new Locale( "ru", "RU" ).equals( locale ) || new Locale( "en", "US" ).equals( locale ) || new Locale( "ko", "KO" ).equals( locale );
        }

        public void setLocale(Locale locale) {
            if ( !isLocaleSupported(locale ))
                throw new UnsupportedOperationException( "Specified locale is not supported yet." );
            this.locale = locale;
        }

        /**
         * Supported locales:
         *
         * @param context
         * @param locale
         */
        public MonthsAdapter( Context context, Locale locale) {
            super( context, 1, 12 );
            setLocale( locale );
        }

        @Override
        public CharSequence getItemText( int index ) {
            if ( index >= 0 && index < getItemsCount() ) {
                int value = 1 + index;
                Assert.assertTrue(value >= 1 && value <= 12);
                if (locale.equals( new Locale( "ru", "RU" ) )) {
                    switch ( value ) {
                        case 1: return "январь";
                        case 2: return "февраль";
                        case 3: return "март";
                        case 4:return "апрель";
                        case 5:return "май";
                        case 6:return "июнь";
                        case 7:return "июль";
                        case 8:return "август";
                        case 9:return "сентябрь";
                        case 10:return "октябрь";
                        case 11:return "ноябрь";
                        case 12:return "декабрь";
                    }
                } else if (locale .equals( new Locale( "en", "US" ))) {
                    switch ( value ) {
                        case 1: return "january";
                        case 2: return "february";
                        case 3: return "march";
                        case 4:return "april";
                        case 5:return "may";
                        case 6:return "june";
                        case 7:return "july";
                        case 8:return "august";
                        case 9:return "september";
                        case 10:return "october";
                        case 11:return "november";
                        case 12:return "december";
                    }
                } else if (locale .equals( new Locale( "ko", "KO" ))) {
                    switch ( value ) {
                    case 1: return "1월";
                    case 2: return "2월";
                    case 3: return "3월";
                    case 4:return "4월";
                    case 5:return "5월";
                    case 6:return "6월";
                    case 7:return "7월";
                    case 8:return "8월";
                    case 9:return "9월";
                    case 10:return "10월";
                    case 11:return "11월";
                    case 12:return "12월";
                }
            } else
                    Assert.assertTrue( false );
            }
            return null;
        }
    }
}

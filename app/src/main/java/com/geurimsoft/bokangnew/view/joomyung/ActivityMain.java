package com.geurimsoft.bokangnew.view.joomyung;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.view.BackPressHandler;
import com.geurimsoft.bokangnew.view.ViewPagerAdapter;
import com.geurimsoft.bokangnew.view.dailyfragment.JoomyungDailyPagerFragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ActivityMain extends AppCompatActivity {

    private ViewPager2 mViewPager;
    private ViewPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;

    private String[] titles = new String[]{"리스트", "웹뷰", "연락처"};

    String code;
    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_main);

        backPressHandler = new BackPressHandler(this);

        Fragment frag1 = new JoomyungDailyPagerFragment();
        Fragment frag2 = new JoomyungDailyPagerFragment();
        Fragment frag3 = new JoomyungDailyPagerFragment();

        mViewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);

        myPagerAdapter = new ViewPagerAdapter(this);
        myPagerAdapter.addFrag(frag1);
        myPagerAdapter.addFrag(frag2);
        myPagerAdapter.addFrag(frag3);

        mViewPager.setAdapter(myPagerAdapter);

        //displaying tabs
        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) -> tab.setText(titles[position])).attach();

    }

}

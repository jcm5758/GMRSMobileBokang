package com.geurimsoft.bokangnew.view.joomyung;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.geurimsoft.bokangnew.R;

import com.geurimsoft.bokangnew.data.GSConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_main);

        Intent intent = getIntent();
        String branName = intent.getStringExtra("branName");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(branName);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Fragment frag1 = new FragmentDailyMain();
        Fragment frag2 = new FragmentDailyMain();
        Fragment frag3 = new FragmentDailyMain();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag1).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.tab1:
                        Toast.makeText(getApplicationContext(),"첫번째",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag1).commit();
                        return true;

                    case R.id.tab2:
                        Toast.makeText(getApplicationContext(),"두번째",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag2).commit();
                        return true;

                    case R.id.tab3:
                        Toast.makeText(getApplicationContext(),"세번째",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag3).commit();
                        return true;

                }

                return false;

            }

        });

    }

}

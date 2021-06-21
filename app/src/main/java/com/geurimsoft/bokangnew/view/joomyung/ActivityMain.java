/**
 * 지점 전체 통계를 한 곳에 모으는 클래스
 *
 * 2021. 05. 28. 리뉴얼
 *
 * Witten by jcm5758
 *
 */

package com.geurimsoft.bokangnew.view.joomyung;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

        GSConfig.activities.add(ActivityMain.this);

        // 인텐트로 지정명 받아 오기
        Intent intent = getIntent();
        String branName = intent.getStringExtra("branName");

        // 액션바 설정 : 제목 타이틀 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(branName);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 일보, 월보, 연보 프래그먼트
        Fragment frag1 = new FragmentDailyMain();
        Fragment frag2 = new FragmentMonthMain();
        Fragment frag3 = new FragmentYearMain();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag1).commit();

        // 메뉴로 아래쪽 탭 구현
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        // 탭 클릭시 프래그먼트 변경
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag1).commit();
                        return true;

                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag2).commit();
                        return true;

                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag3).commit();
                        return true;

                }

                return false;

            }

        });

    }

}

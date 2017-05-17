package com.advanced_android.toolbar_and_tablayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Toolbar를 ActionBar로써 이용
        setSupportActionBar(mToolbar);

        // TabLayout과 ViewPager를 연계
        final MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 페이지가 스크롤됐을 때
            }

            @Override
            public void onPageSelected(int position) {
                // 페이지가 선택됐을 때
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //　페이지 스크롤 상태
            }
        });

        mTabLayout.setupWithViewPager(mViewPager); // TabLayout과 ViewPager를 연결합니다

        //아래 설정으로도 TabLayout과 ViewPager를 연결할 수 있습니다
        //mTabLayout.setTabsFromPagerAdapter(adapter);
        //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // 탭 레이아웃에 탭을 설정(ViewPager를 이용하지 않는 경우)
        // mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_label1));
        // mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_label2));
        // mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_label3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //　ActionBar의 메뉴 아이템을 읽어옵니다
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // ActionBar의 메뉴 아이템이 선택되면 호출됩니다
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "item [" + item + "] selected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}


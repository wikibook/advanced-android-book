package com.advanced_android.coordinatorlayout01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 이 앱은
 * AndroidStudio 템플릿 ScrollingActivity를 바탕으로 합니다.
 *
 */
public class ScrollingActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);

        // ActionBar 설정 
        setSupportActionBar(mToolbar);
    }

    /**
     * ActionBarItem을 셋업 
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    /**
     * ActionBarItem이 탭 됐을 때 콜백된다 
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // 탭된 아이템 ID별로 제어
        if (id == R.id.action_settings) {
            // 설정이 탭 됐을 때 제어를 기술한다 
            Toast.makeText(this,R.string.actionbar_settings_message,Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

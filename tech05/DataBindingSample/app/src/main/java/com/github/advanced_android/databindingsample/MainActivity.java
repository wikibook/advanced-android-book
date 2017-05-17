package com.github.advanced_android.databindingsample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.github.advanced_android.databindingsample.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding 오브젝트를 얻는다
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Binding 오브젝트에 User를 설정한다
        binding.setUser(new User("kim", 25));

        String date = (String) DateFormat.format("yyyy/MM/dd kk:mm:ss", Calendar.getInstance());
        binding.textTime.setText(date);
        //뷰에 id가 지정돼 있으면, Binding 오브젝트로부터 뷰에 대한 참조를 얻을 수 있다
    }
}

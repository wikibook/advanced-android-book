package com.advanced_android.permissiondemob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ActivityB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_b);

        Toast.makeText(getApplicationContext(), "B가 시작", Toast.LENGTH_SHORT).show();
    }
}

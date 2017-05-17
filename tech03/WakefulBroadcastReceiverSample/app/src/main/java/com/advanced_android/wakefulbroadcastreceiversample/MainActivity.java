package com.advanced_android.wakefulbroadcastreceiversample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 이전 설정 클리어
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        setContentView(R.layout.activity_main);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(30 * 1000);
                    sendBroadcast(new Intent("com.advanced_android.wakefulbroadcastreceiversample.TEST_ACTION"));
                } catch (InterruptedException e) {
                }

            }
        })).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("KEY", null);
        TextView textView = (TextView) findViewById(R.id.text);
        if (result == null) {
            textView.setText("잠시 방치해서 슬립상태로 해주세요(30초미만으로 슬립하도록 설정해 두세요）.");
        } else {
            textView.setText("MyIntentService로 처리가 완료");
        }
    }
}

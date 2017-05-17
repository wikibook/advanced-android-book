package com.advanced_android.intentservicesample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    private LocalBroadcastManager mLocalBroadcastManager;
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (FibService.ACTION_CALC_DONE.equals(action)) {
                int result = intent.getIntExtra(FibService.KEY_CALC_RESULT, -1);
                long msec = intent.getLongExtra(FibService.KEY_CALC_MILLISECONDS, -2);
                // 결과 표시
                mTextView.setText("fib(" + FibService.N  + ") = " + result + " (" + msec + ")밀리초" );
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        mIntentFilter = new IntentFilter(FibService.ACTION_CALC_DONE);
        Intent serviceIntent = new Intent(FibService.ACTION_CALC);
        serviceIntent.setClass(getApplicationContext(), FibService.class);
        startService(serviceIntent);

        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText("fib(" + FibService.N + ") 계산 중...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Local BroadcastReceiver를 받도록 등록
        mLocalBroadcastManager.registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 등록한 Local BroadcastReceiver 히제
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }
}

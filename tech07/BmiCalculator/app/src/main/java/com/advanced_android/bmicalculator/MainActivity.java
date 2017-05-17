package com.advanced_android.bmicalculator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LocalBroadcastManager mLocalBroadcastManager;
    private Button mCalcButton;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        initViews();
    }

    @VisibleForTesting
    void initViews() {
        EditText weightText = (EditText)findViewById(R.id.text_weight);
        EditText heightText = (EditText)findViewById(R.id.text_height);
        TextView resultText = (TextView)findViewById(R.id.text_result);

        mCalcButton = (Button)findViewById(R.id.button_calculate);

        View.OnClickListener buttonListener = createButtonListener(weightText, heightText, resultText);
        mCalcButton.setOnClickListener(buttonListener);
    }

    @VisibleForTesting
    View.OnClickListener createButtonListener(final TextView weightText,
                                              final TextView heightText,
                                              final TextView resultText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //결과 취득과 표시
                BmiValue result = calculateBmiValue(weightText, heightText);
                showCalcResult(resultText, result);

                //Service를 사용해 보존 처리
                startResultSaveService(result);
                prepareReceiveResultSaveServiceAction();
            }
        };
    }

    @VisibleForTesting
    BmiValue calculateBmiValue(final TextView weightText, final TextView heightText) {
        float weight = Float.valueOf(weightText.getText().toString());
        float height = Float.valueOf(heightText.getText().toString());
        BmiCalculator calculator = new BmiCalculator();
        return calculator.calculate(height, weight);
    }

    @VisibleForTesting
    void showCalcResult(TextView resultText, BmiValue result) {
        String message = result.toFloat() + " : " + result.getMessage() + "체형입니다";
        resultText.setText(message);
    }

    @VisibleForTesting
    void startResultSaveService(BmiValue result) {
        mCalcButton.setText("저장 중입니다...");
        mCalcButton.setEnabled(false);
        SaveBmiService.start(MainActivity.this, result);
    }

    @VisibleForTesting
    void prepareReceiveResultSaveServiceAction() {
        IntentFilter filter = new IntentFilter(SaveBmiService.ACTION_RESULT);
        mReceiver = new BmiSaveResultReceiver(mCalcButton);
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            mLocalBroadcastManager.unregisterReceiver(mReceiver);
        }
    }

    @VisibleForTesting
    static class BmiSaveResultReceiver extends BroadcastReceiver {

        private Button mCalcButton;

        BmiSaveResultReceiver(Button calcButton) {
            mCalcButton = calcButton;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }

            if (!intent.hasExtra(SaveBmiService.PARAM_RESULT)) {
                return;
            }

            boolean result = intent.getBooleanExtra(SaveBmiService.PARAM_RESULT, false);
            if (!result) {
                Toast.makeText(context, "BMI 저장에 실패했습니다", Toast.LENGTH_SHORT).show();
            }

            mCalcButton.setText("계산한다");
            mCalcButton.setEnabled(true);
        }
    }
}

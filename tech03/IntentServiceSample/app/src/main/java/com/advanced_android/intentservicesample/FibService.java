package com.advanced_android.intentservicesample;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;


/**
 *  피보나치 수열을 계산하는 IntentService
 *
 *  onHandleIntent 안은 워커 스레드(메인 스레드가 아닌）에서 실행됩니다
 */
public class FibService extends IntentService {

    // 서비스 액션
    static final String ACTION_CALC = "ACTION_CALC";
    // 브로드캐스트 액션
    static final String ACTION_CALC_DONE = "ACTION_CALC_DONE";
    // 브로드캐스트로 계산 결과를 주고받기 위한 키
    static final String KEY_CALC_RESULT = "KEY_CALC_RESULT";
    // 브로드캐스트로 계산에 걸린 시간(초)을 주고받기 위한 키
    static final String KEY_CALC_MILLISECONDS = "KEY_CALC_MILLISECONDS";

    // 피보나치 수열 계산
    static final int N = 40;

    public FibService() {
        super("FibService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CALC.equals(action)) {
                long start = System.nanoTime();
                int result = fib(N); // 피보나치 수열 계산
                long end = System.nanoTime();
                Intent resultIntent = new Intent(ACTION_CALC_DONE);
                // 결과를 Intent에 부여
                resultIntent.putExtra(KEY_CALC_RESULT, result);
                resultIntent.putExtra(KEY_CALC_MILLISECONDS, (end - start) / 1000 / 1000);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(resultIntent);
            }
        }
    }

    /** 피보나치 수열 계산 */
    private static int fib(int n) {
        return n <= 1 ? n : fib(n - 1) + fib(n - 2);
    }
}

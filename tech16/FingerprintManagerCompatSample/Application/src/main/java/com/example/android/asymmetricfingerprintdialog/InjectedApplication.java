package com.example.android.asymmetricfingerprintdialog;

import android.app.Application;
import android.util.Log;

import dagger.ObjectGraph;

/**
 * 예제의 애플리케이션 클래스입니다.DI라이브러리 Dagger의 초기화 처리를 하지만 생략합니다.
 */
public class InjectedApplication extends Application {

    private static final String TAG = InjectedApplication.class.getSimpleName();

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        initObjectGraph(new FingerprintModule(this));
    }

    /**
     * DI라이브러리 Dagger의 초기화 처리입니다
     *
     * @param module for Dagger
     */
    public void initObjectGraph(Object module) {
        mObjectGraph = module != null ? ObjectGraph.create(module) : null;
    }

    public void inject(Object object) {
        if (mObjectGraph == null) {
            // This usually happens during tests.
            Log.i(TAG, "Object graph is not initialized.");
            return;
        }
        mObjectGraph.inject(object);
    }

}

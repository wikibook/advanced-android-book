package com.advanced_android.musicplayersample;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BackgroundMusicService extends Service {

    private static final String TAG = BackgroundMusicService.class.getSimpleName();

    private final IBinder mBinder = new MyBinder();
    private MediaPlayer mPlayer;

    public BackgroundMusicService() {
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.d("service","onBind");
        return mBinder;
    }

    public class MyBinder extends Binder {
        BackgroundMusicService getService() {
            return BackgroundMusicService.this;
        }
    }

    /**
     * 음악재생중인지 아닌지 반환한다
     *
     * @return 음악재생중인 경우는 true. 나머지는 false.
     */
    public boolean isPlaying() {
        boolean isPlaying = false;
        if (mPlayer != null) {
            isPlaying = mPlayer.isPlaying();
        }
        return isPlaying;
    }

    /**
     * 음악을 재생한다
     */
    public void play() {
        Log.d(TAG, "play");
        mPlayer = MediaPlayer.create(this, R.raw.bensound_clearday);
        mPlayer.setLooping(true); // Set looping
        mPlayer.setVolume(100, 100);
        mPlayer.start();
    }

    /**
     * 음악을 정지한다. 모두 정지 중인 경우는 아무것도 하지 않는다
     */
    public void stop() {
        Log.d(TAG, "stop");
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
}

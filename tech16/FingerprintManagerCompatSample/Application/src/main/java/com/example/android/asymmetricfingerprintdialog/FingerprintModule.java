package com.example.android.asymmetricfingerprintdialog;

import com.example.android.asymmetricfingerprintdialog.server.StoreBackend;
import com.example.android.asymmetricfingerprintdialog.server.StoreBackendImpl;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.inputmethod.InputMethodManager;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for Fingerprint APIs.
 * DI 라이브러리 Dagger에 의한 클래스 모듈 클래스. 설명은 생략합니다.
 */
@Module(
        library = true,
        injects = {MainActivity.class}
)
public class FingerprintModule {

    private final Context mContext;

    public FingerprintModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context providesContext() {
        return mContext;
    }

    @Provides
    public FingerprintManagerCompat providesFingerprintManager(Context context) {
        return FingerprintManagerCompat.from(context);
    }

    @Provides
    public KeyguardManager providesKeyguardManager(Context context) {
        return (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Provides
    public KeyStore providesKeystore() {
        try {
            return KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
    }

    @Provides
    public KeyPairGenerator providesKeyPairGenerator() {
        try {
            // Google의 예제 "EC" 알고리즘은 targetSdk=23이하에서 이용할 수 없으므로
            // 23미만에서는 편의상 "RSA"를 지정합니다.
            String algorithm = Build.VERSION.SDK_INT >= 23 ? "EC" : "RSA";
            return KeyPairGenerator.getInstance(algorithm, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyPairGenerator", e);
        }
    }

    @Provides
    public Signature providesSignature(KeyStore keyStore) {
        try {
            return Signature.getInstance("SHA256withECDSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get an instance of Signature", e);
        }
    }

    @Provides
    public InputMethodManager providesInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Provides
    public SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    public StoreBackend providesStoreBackend() {
        return new StoreBackendImpl();
    }
}

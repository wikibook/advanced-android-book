package com.advanced_android.uiautometertest;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class UiAutometorSampleTest {

    private static final String GOOGLE_PLAY_PACKAGE = "com.android.vending";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // UiDevice를 초기화하고 홈버튼을 누른다
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();

        // 런쳐앱의 시작을 기다린다
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);
    }

    @Test
    public void GooglePlay를시작하고텍스트입력후에검색화면으로이동할수있다() throws UiObjectNotFoundException {
        // Google Play를 시작한다
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(GOOGLE_PLAY_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Google Play 시작을 기다린다
        mDevice.wait(Until.hasObject(By.pkg(GOOGLE_PLAY_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);

        // 검색박스 이미지를 클릭한다
        UiObject searchBoxImage = mDevice.findObject(new UiSelector()
                .resourceId("com.android.vending:id/search_box_idle_text")
                .className("android.widget.ImageView"));
        searchBoxImage.click();

        // 검색을 위한 EditText가 표시된다
        UiObject searchBox = mDevice.findObject(new UiSelector()
                .resourceId("com.android.vending:id/search_box_text_input")
                .className("android.widget.EditText"));
        assertTrue(searchBox.exists());
    }

}

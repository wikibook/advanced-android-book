package com.advanced_android.basicespressotest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainScreenTest {

    //fab를 클릭한 뒤에 표시되는 메시지
    final String MESSAGE = MainActivity.DONE_MESSAGE;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void 화면에FAB관련문자열이표시되지않는다() {
        onView(withText(MESSAGE)).check(doesNotExist()); //메시지가 표시되지 않았다
    }

    @Test
    public void FAB를클릭하면FAB관련문자열이표시된다() {
        onView(withId(R.id.fab)).perform(click()); //FAB를릭
        onView(withText(MESSAGE)).check(matches(isDisplayed())); //메시지가 표시되어 있다
    }

}

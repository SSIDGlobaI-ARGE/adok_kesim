package com.ardayucesan.gezderi_uretim;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.content.Context;


import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.ardayucesan.gezderi_uretim.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    ViewInteraction viewInteraction = onView(withText(R.string.giri_yapmak_in_kart_okutunuz));

    @Test
    public void view_isCorrect() {
        onView(withId(R.id.tv_status)).check(matches(isDisplayed()));
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.@Rule

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.ardayucesan.gezderi_kaplama", appContext.getPackageName());
    }
}

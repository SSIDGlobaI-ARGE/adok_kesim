package com.ardayucesan.gezderi_uretim.ui.login;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.lifecycle.Lifecycle.State;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class LoginPresenterTest {
    private LoginPresenter loginPresenter;

    @Before void setUp() {

    }

    @Test
    public void testLogin() {
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.moveToState(State.STARTED);    // Moves the activity state to State.STARTED.
        scenario.onActivity(activity -> {
            assertThat(activity.versionName, is("1.9"));
        });
    }


    @Test
    public void testFetchUpdate() {

    }

}
package org.secuso.privacyfriendlyactivitytracker.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assume.assumeTrue;

import android.Manifest;
import android.app.Activity;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.secuso.privacyfriendlyactivitytracker.R;
import org.secuso.privacyfriendlyactivitytracker.TestUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import kotlin.jvm.JvmField;

public class PreferencesActivityTest {

    @Rule
    public GrantPermissionRule activityRecognitionPermission = (android.os.Build.VERSION.SDK_INT >= 29 ? GrantPermissionRule.grant(Manifest.permission.ACTIVITY_RECOGNITION) : null);
    @Rule
    public GrantPermissionRule writeExternalPermission = (android.os.Build.VERSION.SDK_INT < 19 ? GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE) : null);
    @Rule
    public GrantPermissionRule foregroundServiceHealthPermission = GrantPermissionRule.grant(Manifest.permission.FOREGROUND_SERVICE_HEALTH);
    @Rule
    public GrantPermissionRule bodySensorsPermission = GrantPermissionRule.grant(Manifest.permission.BODY_SENSORS);

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    Activity mActivity;
    private final String tag = getClass().getSimpleName();

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity -> mActivity = activity);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_settings)).perform(click());
    }

    @Test
    public void exportCsv() {
        assumeTrue(TestUtils.isExternalStorageAvailable());
        AtomicReference<Object> csvObjectReference = new AtomicReference<>();
        onView(withText(R.string.pref_header_general)).perform(click());
        PreferencesActivity.GeneralPreferenceFragment generalPreferenceFragment = PreferencesActivity.getGeneralPreferenceFragment();
        getInstrumentation().runOnMainSync(() -> csvObjectReference.set(generalPreferenceFragment.generateCSVToExport()));
        Object csvFile = csvObjectReference.get();
        assertThat("CSV export could not be created", csvFile, notNullValue());
        /*
        String csvContent = TestUtils.readFileToString(csvFile);
        assertThat("CSV header is not as expected", csvContent, startsWith(TestUtils.getResourceString(R.string.export_csv_header))); */
    }
}
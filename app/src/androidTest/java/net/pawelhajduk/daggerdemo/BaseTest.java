package net.pawelhajduk.daggerdemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import com.novoda.rxpresso.RxPresso;

import net.pawelhajduk.daggerdemo.TestModules.TestApiModule;
import net.pawelhajduk.daggerdemo.TestModules.TestDataModule;

import org.junit.Rule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

//This is base test class, which contains no tests, but is extended by every other test class
//It contains proper @Rule with setUp and tearDown

public class BaseTest {
    private static final long LAUNCH_TIMEOUT = 1000;
    private static RxPresso rxPresso;
    private static DaggerDemoComponent testComponent;

    public static RxPresso getRxPresso() {
        return rxPresso;
    }

    public static DaggerDemoComponent getTestComponent() {
        return testComponent;
    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected void beforeActivityLaunched() {

            //change dagger graph
            DaggerDemoApplication globalApplication = (DaggerDemoApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
            testComponent = DaggerDaggerDemoComponent.builder()
                                                     .debugApiModule(new TestApiModule())
                                                     .dataModule(new TestDataModule())
                                                     .mainModule(new MainModule(globalApplication))
                                                     .build();
            DaggerDemoApplication.setGraph(testComponent);


            //initialize RxPresso
            rxPresso = new RxPresso(testComponent.getGithubService());
            Espresso.registerIdlingResources(rxPresso);
            try {
                unlockDevice();
            } catch(Exception e) {

            }
        }

        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            Espresso.unregisterIdlingResources(rxPresso);
            rxPresso.resetMocks();
            clearPrefs();
        }
    };

    private void unlockDevice() {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        try {
            if(!mDevice.isScreenOn()) {
                mDevice.wakeUp();
                mDevice.drag(mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() * 3 / 4, mDevice.getDisplayWidth() / 2, 10, 30);
            }
        } catch(RemoteException e) {
            e.printStackTrace();
        }
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    public void rebindMocks() {
        Espresso.unregisterIdlingResources(rxPresso);
        rxPresso.resetMocks();
        rxPresso = new RxPresso(testComponent.getGithubService());
        Espresso.registerIdlingResources(rxPresso);
    }

    public void clearPrefs() {
        testComponent.getSharedPreferences().edit().clear().commit();
    }
}

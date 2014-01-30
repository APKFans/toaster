package org.mars3142.android.toaster.activity;

import android.app.ActionBar;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.mars3142.android.toaster.R;
import org.mars3142.android.toaster.fragment.NavigationDrawerFragment;
import org.mars3142.android.toaster.fragment.ToasterFragment;
import org.mars3142.android.toaster.helper.PackageHelper;
import org.mars3142.android.toaster.listener.AccessibilityServiceListener;
import org.mars3142.android.toaster.listener.DeleteListener;
import org.mars3142.android.toaster.table.ToasterTable;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final static String TAG = "MainActivity";
    private final static String PACKAGE_NAME = "packagename";

    private NavigationDrawerFragment mNavDrawerFragment;
    private CharSequence mTitle;
    private String mPackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTitle = getTitle();

        mNavDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        if (savedInstanceState != null) {
            mPackageName = savedInstanceState.getString(PACKAGE_NAME);
        }

        onNavigationDrawerItemSelected(mPackageName);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PACKAGE_NAME, mPackageName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(String packageFilter) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragmentView = ToasterFragment.newInstance(packageFilter);
        fragmentManager.beginTransaction().replace(R.id.container, fragmentView).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                DeleteListener deleteListener;
                if (mPackageName.length() == 0) {
                    deleteListener = new DeleteListener(this, ToasterTable.TOASTER_URI);
                } else {
                    deleteListener = new DeleteListener(this, ToasterTable.TOASTER_URI, ToasterTable.PACKAGE + " = ?", new String[]{mPackageName});
                }
                Builder builder = new Builder(this);
                builder.setTitle(R.string.action_delete);
                builder.setMessage(R.string.delete_question);
                builder.setCancelable(true);
                builder.setPositiveButton(android.R.string.ok, deleteListener);
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isAccessibilitySettingsOn(getApplicationContext())) {
            Builder builder = new Builder(this);
            builder.setTitle(R.string.toaster_service_header);
            builder.setMessage(R.string.toaster_service_message);
            builder.setCancelable(true);
            builder.setPositiveButton(android.R.string.ok, new AccessibilityServiceListener(this));
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.create().show();
        }
    }

    public void onSectionAttached(String packageName) {
        if (packageName.length() != 0) {
            mTitle = PackageHelper.getAppName(getApplicationContext(), packageName);
        } else {
            mTitle = getString(R.string.all_data);
        }
        mPackageName = packageName;
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "org.mars3142.android.toaster/org.mars3142.android.toaster.service.ToasterService";
        boolean accessibilityFound = false;

        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String serviceName = splitter.next();
                    if (serviceName.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return accessibilityFound;
    }
}

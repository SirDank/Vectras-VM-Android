/*
Copyright (C) Max Kastanas 2012

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.vectras.qemu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import com.vectras.vm.R;
import com.vectras.vm.SplashActivity;

import java.util.List;

public class MainSettingsManager extends AppCompatActivity
        implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    public static MainSettingsManager activity;

    private static Handler mHandler;
    public static SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        activity = this;

        sp = PreferenceManager.getDefaultSharedPreferences(activity);

        PreferenceFragmentCompat preference = new MainPreferencesFragment();
        Intent intent = getIntent();

        // add preference settings
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settingz, preference)
                .commit();

        // toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle bundle = pref.getExtras();
        final Fragment fragment = Fragment.instantiate(this, pref.getFragment(), bundle);

        fragment.setTargetFragment(caller, 0);

        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settingz, fragment)
                .addToBackStack(null)
                .commit();

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class MainPreferencesFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onResume() {
            super.onResume();

        }

        @Override
        public void onPause() {
            super.onPause();

        }


        @Override
        public void onCreatePreferences(Bundle bundle, String root_key) {
            // Load the Preferences from the XML file
            setPreferencesFromResource(R.xml.headers_preference, root_key);

        }

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {
            if (pref.getKey().equals("app")) {

            }
            return true;
        }

    }


    public static class AppPreferencesFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onResume() {
            super.onResume();

        }

        @Override
        public void onPause() {
            super.onPause();

        }


        @Override
        public void onCreatePreferences(Bundle bundle, String root_key) {
            // Load the Preferences from the XML file
            setPreferencesFromResource(R.xml.settings, root_key);

        }

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {
            if (pref.getKey().equals("app")) {

            }
            return true;
        }

    }

    public static class UserInterfacePreferencesFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mHandler = new Handler();
            Preference pref = findPreference("modeNight");
            if (pref != null) {
                pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(@NonNull Preference preference,
                                                      Object newValue) {
                        onNightMode();
                        return true;
                    }

                });
            }
        }

        private void onNightMode() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent startActivity = new Intent(getContext(), SplashActivity.class);
                    int pendingIntentId = 123456;
                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), pendingIntentId, startActivity, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    AlarmManager mgr = (AlarmManager) MainSettingsManager.activity.getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);

                    System.exit(0);
                }
            }, 300);
        }

        @Override
        public void onResume() {
            super.onResume();

        }

        @Override
        public void onPause() {
            super.onPause();

        }


        @Override
        public void onCreatePreferences(Bundle bundle, String root_key) {
            // Load the Preferences from the XML file
            setPreferencesFromResource(R.xml.userinterface, root_key);

        }

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {
            return true;
        }

    }

    public static class QemuPreferencesFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Preference pref = findPreference("customMemory");
            if (pref != null) {
                pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(@NonNull Preference preference,
                                                      Object newValue) {
                        findPreference("memory").setEnabled(!sp.getBoolean("customMemory", false));
                        return true;
                    }

                });
            }
            Preference pref2 = findPreference("customMemory");
            if (pref2 != null) {
                pref2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(@NonNull Preference preference,
                                                      Object newValue) {
                        if (MainSettingsManager.getVirtio(activity)) {
                            Config.hd_if_type = "virtio";
                        } else {
                            Config.hd_if_type = "ide";
                        }
                        return true;
                    }

                });
            }
        }

        private void onMemory() {
            findPreference("memory").setEnabled(sp.getBoolean("customMemory", false));
        }

        @Override
        public void onResume() {
            super.onResume();
            onMemory();
        }

        @Override
        public void onPause() {
            super.onPause();
            onMemory();
        }


        @Override
        public void onCreatePreferences(Bundle bundle, String root_key) {
            // Load the Preferences from the XML file
            setPreferencesFromResource(R.xml.qemu, root_key);

        }

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {

            return true;
        }

    }

    public static class VncPreferencesFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onResume() {
            super.onResume();

        }

        @Override
        public void onPause() {
            super.onPause();

        }


        @Override
        public void onCreatePreferences(Bundle bundle, String root_key) {
            // Load the Preferences from the XML file
            setPreferencesFromResource(R.xml.vnc, root_key);

        }

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {
            if (pref.getKey().equals("app")) {

            }
            return true;
        }

    }

    static String getDNSServer(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("dnsServer", Config.defaultDNSServer);
    }

    public static void setDNSServer(Activity activity, String dnsServer) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("dnsServer", dnsServer);
        edit.apply();
    }

    public static boolean getVncExternal(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("vncExternal", false);
    }

    public static void setVncExternal(Activity activity, boolean vncExternal) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("vncExternal", vncExternal);
        edit.apply();
    }

    public static int getOrientationSetting(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        int orientation = prefs.getInt("orientation", 0);
        // UIUtils.log("Getting First time: " + firstTime);
        return orientation;
    }

    public static void setOrientationSetting(Activity activity, int orientation) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("orientation", orientation);
        edit.apply();
    }


    public static boolean getPromptUpdateVersion(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("updateVersionPrompt", Config.defaultCheckNewVersion);
    }


    public static void setPromptUpdateVersion(Activity activity, boolean flag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("updateVersionPrompt", flag);
        edit.apply();
        // UIUtils.log("Setting First time: ");
    }

    static boolean getPrio(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("HighPrio", false);
    }

    public static void setPrio(Activity activity, boolean flag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("HighPrio", flag);
        edit.apply();
        // UIUtils.log("Setting First time: ");
    }

    public static boolean getAlwaysShowMenuToolbar(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("AlwaysShowMenuToolbar", false);
    }

    public static void setAlwaysShowMenuToolbar(Activity activity, boolean flag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("AlwaysShowMenuToolbar", flag);
        edit.apply();
        // UIUtils.log("Setting First time: ");
    }

    public static boolean getFullscreen(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("ShowFullscreen", true);
    }

    public static void setFullscreen(Activity activity, boolean flag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("ShowFullscreen", flag);
        edit.apply();
        // UIUtils.log("Setting First time: ");
    }

    public static boolean getDesktopMode(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("DesktopMode", false);
    }

    public static void setDesktopMode(Activity activity, boolean flag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("DesktopMode", flag);
        edit.apply();
        // UIUtils.log("Setting First time: ");
    }

    public static boolean getEnableLegacyFileManager(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("EnableLegacyFileManager", false);
    }


    public static void setEnableLegacyFileManager(Activity activity, boolean flag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("EnableLegacyFileManager", flag);
        edit.apply();
        // UIUtils.log("Setting First time: ");
    }

    public static String getLastDir(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String imagesDir = prefs.getString("lastDir", null);
        return imagesDir;
    }

    public static void setLastDir(Context context, String imagesPath) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("lastDir", imagesPath);
        edit.commit();
    }

    public static String getImagesDir(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String imagesDir = prefs.getString("imagesDir", null);
        return imagesDir;
    }

    public static void setImagesDir(Context context, String imagesPath) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("imagesDir", imagesPath);
        edit.commit();
    }


    public static String getExportDir(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String imagesDir = prefs.getString("exportDir", null);
        return imagesDir;
    }

    public static void setExportDir(Context context, String imagesPath) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("exportDir", imagesPath);
        edit.commit();
    }


    public static String getSharedDir(Context context) {
        String lastDir = Environment.getExternalStorageDirectory().getPath();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("sharedDir", lastDir);
    }

    public static void setSharedDir(Context context, String lastDir) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("sharedDir", lastDir);
        edit.apply();
        // UIUtils.log("Setting First time: ");
    }


    public static Boolean getMTTCG(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean MTTCG = prefs.getBoolean("MTTCG", true);
        return MTTCG;
    }

    public static void setMTTCG(Context context, Boolean MTTCG) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("MTTCG", MTTCG);
        edit.commit();
    }

    public static int getCpuCores(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int cpuCores = prefs.getInt("cpuCores", 1);
        return cpuCores;
    }

    public static void setCpuCores(Context context, int cpuCores) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("cpuCores", cpuCores);
        edit.commit();
    }

    public static int getExitCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int exitCode = prefs.getInt("exitCode", 1);
        return exitCode;
    }

    public static void setExitCode(Context context, int exitCode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("exitCode", exitCode);
        edit.commit();
    }

    public static int getCpuNum(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int cpuNum = Integer.parseInt(prefs.getString("cpuNum", "1"));
        return cpuNum;
    }

    public static void setCpuNum(Context context, String cpuNum) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("cpuNum", cpuNum);
        edit.commit();
    }

    public static String getControlMode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String controlMode = prefs.getString("controlMode", "D");
        return controlMode;
    }

    public static void setControlMode(Context context, String controlMode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("controlMode", controlMode);
        edit.commit();
    }


    public static void setModeNight(Context context, Boolean nightMode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("modeNight", nightMode);
        edit.commit();
    }

    public static Boolean getModeNight(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("modeNight", false);
    }

    public static void setCusRam(Activity activity, Boolean cusRam) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("customMemory", cusRam);
        edit.apply();
    }

    public static boolean getCusRam(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("customMemory", false);
    }

    public static void setVirtio(Activity activity, Boolean virtio) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("virtio", virtio);
        edit.apply();
    }

    public static boolean getVirtio(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("virtio", false);
    }
    public static boolean isFirstLaunch(Activity activity) {
        PackageInfo pInfo = null;

        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getClass().getPackage().getName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean firstTime = prefs.getBoolean("firstTime" + pInfo.versionName, true);
        return firstTime;
    }

    public static void setFirstLaunch(Activity activity) {
        PackageInfo pInfo = null;

        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getClass().getPackage().getName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("firstTime" + pInfo.versionName, false);
        edit.commit();
    }

}
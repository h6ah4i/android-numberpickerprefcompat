package com.h6ah4i.android.example.numberpickerpreferencecompat;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.h6ah4i.android.preference.NumberPickerPreferenceCompat;
import com.h6ah4i.android.preference.NumberPickerPreferenceDialogFragmentCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MyPreferenceFragment())
                .commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragmentCompat {
        private static final String DIALOG_FRAGMENT_TAG =
                "android.support.v7.preference.PreferenceFragment.DIALOG";

        public MyPreferenceFragment() {
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            // check if dialog is already showing
            if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                return;
            }

            final DialogFragment f;

            if (preference instanceof NumberPickerPreferenceCompat) {
                f = NumberPickerPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            } else {
                f = null;
            }

            if (f != null) {
                f.setTargetFragment(this, 0);
                f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }
    }
}

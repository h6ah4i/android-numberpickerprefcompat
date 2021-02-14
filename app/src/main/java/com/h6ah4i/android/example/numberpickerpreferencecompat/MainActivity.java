package com.h6ah4i.android.example.numberpickerpreferencecompat;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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
                "androidx.preference.PreferenceFragment.DIALOG";

        public MyPreferenceFragment() {
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
            findPreference("number_picker_preference_1").setSummaryProvider(new Preference.SummaryProvider() {
                @Override
                public CharSequence provideSummary(Preference preference) {
                    NumberPickerPreferenceCompat numPickerPreference = (NumberPickerPreferenceCompat) preference;
                    return "Summary text goes here (value = " + numPickerPreference.getValue() + " " + numPickerPreference.getUnitText() + ")";
                }
            });
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

package com.h6ah4i.android.example.numberpickerpreferencecompat;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);

            // basic
            requireNumberPreference("number_picker_preference_1").setSummaryProvider(new Preference.SummaryProvider<NumberPickerPreferenceCompat>() {
                @Override
                public CharSequence provideSummary(NumberPickerPreferenceCompat preference) {
                    return "Summary text goes here (value = " + preference.getValue() + " " + preference.getUnitText() + ")";
                }
            });

            // with android:entries
            requireNumberPreference("number_picker_preference_2").setSummaryProvider(new Preference.SummaryProvider<NumberPickerPreferenceCompat>() {
                @Override
                public CharSequence provideSummary(NumberPickerPreferenceCompat preference) {
                    return "Summary text goes here (value = " + preference.getEntries()[preference.getValue() - preference.getMinValue()] + " " + preference.getUnitText() + ")";
                }
            });

            // negative values
            requireNumberPreference("number_picker_preference_3").setSummaryProvider(new Preference.SummaryProvider<NumberPickerPreferenceCompat>() {
                @Override
                public CharSequence provideSummary(NumberPickerPreferenceCompat preference) {
                    return "Summary text goes here (value = " + preference.getValue() +")";
                }
            });

            // configure programmatically
            NumberPickerPreferenceCompat pref4 = requireNumberPreference("number_picker_preference_4");
            pref4.setTitle("Configure programmatically");
            pref4.setDialogTitle("Configure programmatically");
            pref4.setEntries(new String[]{"foo", "bar", "baz"});
            pref4.setMinValue(0);
            pref4.setMaxValue(2);
            pref4.setSummaryProvider(new Preference.SummaryProvider<NumberPickerPreferenceCompat>() {
                @Override
                public CharSequence provideSummary(NumberPickerPreferenceCompat preference) {
                    return "Summary text goes here (value = " + preference.getEntries()[preference.getValue() - preference.getMinValue()] + ")";
                }
            });
        }

        @NonNull
        private NumberPickerPreferenceCompat requireNumberPreference(@NonNull CharSequence key) {
            return (NumberPickerPreferenceCompat) findPreference(key);
        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            // check if dialog is already showing
            if (getParentFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                return;
            }

            final DialogFragment f = preference instanceof NumberPickerPreferenceCompat ? NumberPickerPreferenceDialogFragmentCompat.newInstance(preference.getKey()) : null;

            if (f != null) {
                f.setTargetFragment(this, 0);
                f.show(getParentFragmentManager(), DIALOG_FRAGMENT_TAG);
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }
    }
}

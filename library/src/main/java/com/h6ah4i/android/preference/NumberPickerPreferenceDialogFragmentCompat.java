/*
 * Copyright (C) 2017 Haruki Hasegawa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.h6ah4i.android.preference;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.h6ah4i.android.preference.numberpickercompat.R;
import com.h6ah4i.android.widget.numberpickercompat.NumberPicker;

public class NumberPickerPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private static final String SAVE_STATE_VALUE = "NumberPickerPreferenceDialogFragmentCompat.value";
    private NumberPicker mNumberPicker;
    private TextView mUnitTextView;
    private int mValue;

    @NonNull
    public static NumberPickerPreferenceDialogFragmentCompat newInstance(@NonNull String key) {
        final NumberPickerPreferenceDialogFragmentCompat fragment = new NumberPickerPreferenceDialogFragmentCompat();
        final Bundle args = new Bundle(1);
        args.putString(ARG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mValue = getNumberPickerPreference().getValue();
        } else {
            mValue = savedInstanceState.getInt(SAVE_STATE_VALUE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_VALUE, mValue);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);

        mNumberPicker = view.findViewById(R.id.nppc_number_picker);
        mUnitTextView = view.findViewById(R.id.nppc_unit_text);

        if (mNumberPicker == null) {
            throw new IllegalStateException("Dialog view must contain an NumberPicker with id" +
                    " @id/nppc_number_picker");
        }

        mNumberPicker.setMinValue(getNumberPickerPreference().getMinValue());
        mNumberPicker.setMaxValue(getNumberPickerPreference().getMaxValue());
        CharSequence[] entries = getNumberPickerPreference().getEntries();
        if (entries != null) {
            mNumberPicker.setDisplayedValues(mapToStringArray(entries));
        }
        mNumberPicker.setValue(mValue);

        mNumberPicker.setWrapSelectorWheel(getNumberPickerPreference().getWrapSelectorWheel());

        final CharSequence unitText = getNumberPickerPreference().getUnitText();
        if (unitText != null) {
            mUnitTextView.setText(unitText);
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // NOTE: this clearFocus() triggers validateInputTextView() internally
            mNumberPicker.clearFocus();

            final int value = mNumberPicker.getValue();
            if (getNumberPickerPreference().callChangeListener(value)) {
                getNumberPickerPreference().setValue(value);
            }
        }
    }

    private NumberPickerPreferenceCompat getNumberPickerPreference() {
        return (NumberPickerPreferenceCompat) getPreference();
    }

    private static String[] mapToStringArray(CharSequence[] entries) {
        String[] converted = new String[entries.length];
        for (int i = 0; i < entries.length; i++) {
            converted[i] = entries[i].toString();
        }
        return converted;
    }
}

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

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

import com.h6ah4i.android.preference.numberpickercompat.R;

public class NumberPickerPreferenceCompat extends DialogPreference {
    private int mValue;
    private int mMaxValue = Integer.MAX_VALUE;
    private int mMinValue = Integer.MIN_VALUE;
    private boolean mValueSet;
    private String mUnitText = null;
    private boolean mWrapSelectorWheel = true;
    private String[] mEntries = null;

    public NumberPickerPreferenceCompat(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public NumberPickerPreferenceCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public NumberPickerPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public NumberPickerPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.obtainStyledAttributes(
                attrs, R.styleable.nppc_NumberPickerPreferenceCompat, defStyleAttr, defStyleRes);

        mMinValue = ta.getInt(R.styleable.nppc_NumberPickerPreferenceCompat_nppc_minValue, mMinValue);
        mMaxValue = ta.getInt(R.styleable.nppc_NumberPickerPreferenceCompat_nppc_maxValue, mMaxValue);
        mUnitText = ta.getString(R.styleable.nppc_NumberPickerPreferenceCompat_nppc_unitText);
        mWrapSelectorWheel = ta.getBoolean(R.styleable.nppc_NumberPickerPreferenceCompat_nppc_wrapSelectorWheel, true);
        CharSequence[] entries = ta.getTextArray(R.styleable.nppc_NumberPickerPreferenceCompat_android_entries);
        if (entries != null) {
            // Convert CharSequence[] to String[]
            mEntries = new String[entries.length];
            for (int i = 0; i < entries.length; i++) mEntries[i] = entries[i].toString();
        }

        ta.recycle();

        setDialogLayoutResource(R.layout.nppc_preference_dialog_number_picker);
    }

    /**
     * Saves the value to the {@link android.content.SharedPreferences}.
     *
     * @param value The value to save
     */
    public void setValue(int value) {
        final boolean changed = value != getValue();

        // Always persist/notify the first time.
        if (changed || !mValueSet) {
            mValue = value;
            mValueSet = true;

            persistInt(value);
            if (changed) {
                notifyChanged();
            }
        }
    }

    /**
     * Gets the value from the {@link android.content.SharedPreferences}.
     *
     * @return The current preference value.
     */
    public int getValue() {
        return mValue;
    }

    public int getMinValue() {
        if (mMinValue == Integer.MIN_VALUE) return 0;
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        mMinValue = minValue;
    }

    public int getMaxValue() {
        if (mMaxValue == Integer.MAX_VALUE) return 0;
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
    }

    public boolean getWrapSelectorWheel() {
        return mWrapSelectorWheel;
    }

    public void setWrapSelectorWheel(boolean wrapSelectorWheel) {
        mWrapSelectorWheel = wrapSelectorWheel;
    }

    @Nullable
    public String[] getEntries() {
        return mEntries;
    }

    public void setEntries(@Nullable String[] entries) {
        mEntries = entries;
    }

    @Nullable
    public String getUnitText() { return mUnitText; };

    public void setUnitText(@Nullable String unitText) {
        mUnitText = unitText;
    }

    @NonNull
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return clamp(a.getInt(index, 0), mMinValue, mMaxValue);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        setValue(getPersistedInt(clamp(((defaultValue != null) ? (Integer) defaultValue : 0), mMinValue, mMaxValue)));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getValue();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.value);
    }

    private static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    private static class SavedState extends BaseSavedState {
        int value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(value);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

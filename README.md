NumberPickerPreferenceCompat
===============

An Android's Preference widget which can pick a number using NumberPicker dialog. This library uses the [ported version of NumberPicker widget](https://github.com/h6ah4i/android-numberpickercompat) and the [AndroidX Preference library](https://developer.android.com/reference/androidx/preference/package-summary) which works perfectly on API level 14+.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.h6ah4i.android.preference/numberpickerprefcompat/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.h6ah4i.android.preference/numberpickerprefcompat)

---

Screenshot
---

<img src="./pic/screenshot.png?raw=true" alt="NumberPickerPreferenceCompat" width="200" />

---

Target platforms
---

- API level 14 or later


Latest version
---

- Version 1.2.2  (September 12, 2021)   ([RELEASE NOTES](./RELEASE-NOTES.md))

Getting started
---

This library is published on Maven Central. Just add these lines to `build.gradle`.

```diff
repositories {
+     mavenCentral()
}

dependencies {
+    implementation 'com.h6ah4i.android.preference:numberpickerprefcompat:1.2.2'
}
```

Usage
---

**In XML**
```xml
<?xml version="1.0" encoding="utf-8"?>
<PreferenceScreen
    xmlns:app="http://schemas.android.com/apk/res-auto"
    >

    <com.h6ah4i.android.preference.NumberPickerPreferenceCompat
        app:key="number_picker_preference_1"
        app:summary="Summary text goes here"
        app:title="Number picker preference"
        app:nppc_maxValue="100"
        app:nppc_minValue="0"
        app:nppc_unitText="%"
        app:nppc_wrapSelectorWheel="false"
        />

</PreferenceScreen>
```

**In Java code**
```java
public class MyPreferenceFragment extends PreferenceFragmentCompat {
    private static final String DIALOG_FRAGMENT_TAG =
            "androidx.preference.PreferenceFragment.DIALOG";

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // check if dialog is already showing
        if (getParentFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
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
            f.show(getParentFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ...
    }
}
```

License
---

This library is licensed under the [Apache Software License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).

See [`LICENSE`](LICENSE) for full of the license text.

    Copyright (C) 2017 Haruki Hasegawa

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

/*
 * Copyright 2017 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opendatakit.counter.utilities;

import android.content.Context;
import android.preference.PreferenceManager;

import static org.opendatakit.counter.activities.CounterActivity.SHARED_PREFS_DEFAULT_VALUE;

public class SharedPreferencesUtils {

    public static void saveValue(Context context, String key, int value) {
        try {
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putInt(key, value)
                    .apply();
        } catch (OutOfMemoryError e) {
            clearSharedPreferences(context);
            saveValue(context, key, value);
        }
    }

    public static int getValue(Context context, String key) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getInt(key, SHARED_PREFS_DEFAULT_VALUE);
    }

    private static void clearSharedPreferences(Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .apply();
    }
}

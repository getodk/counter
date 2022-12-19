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

package org.opendatakit.counter.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import org.opendatakit.counter.R;
import org.opendatakit.counter.utilities.SharedPreferencesUtils;

public class CounterActivity extends AppCompatActivity {
    // https://github.com/opendatakit/collect/blob/master/collect_app/src/main/java/org/odk/collect/android/widgets/ExIntegerWidget.java#L68
    private static final int MAX_VALUE = 999999999;
    private static final int MIN_VALUE = -99999999;
    public static final int SHARED_PREFS_DEFAULT_VALUE = MAX_VALUE + 1;

    private static final String CURRENT_VALUE = "currentValue";
    private static final String FORM_ID = "form_id";
    private static final String FORM_NAME = "form_name";
    private static final String QUESTION_ID = "question_id";
    private static final String QUESTION_NAME = "question_name";
    private static final String INCREMENT = "increment";
    private static final String VALUE = "value";

    private TextSwitcher currentValueTv;

    private Button plusButton;
    private Button minusButton;

    private String formId;
    private String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        setUpLayoutElements();

        formId = getIntent().getStringExtra(FORM_ID);
        questionId = getIntent().getStringExtra(QUESTION_ID);

        if (savedInstanceState != null) {
            currentValueTv.setText(savedInstanceState.getString(CURRENT_VALUE));
        } else if (SharedPreferencesUtils.getValue(this, formId + questionId) != SHARED_PREFS_DEFAULT_VALUE) {
            currentValueTv.setText(String.valueOf(SharedPreferencesUtils.getValue(this, formId + questionId)));
        } else if (getIntent().getBooleanExtra(INCREMENT, false)) {
            currentValueTv.setText(getString(R.string.zero));
        } else {
            currentValueTv.setText(getString(R.string.one));
        }

        setupAnimation();

        int currentValue = getCurrentValue();
        adjustTextSize(currentValue);
        disableButtonIfNeeded(currentValue);

        if (savedInstanceState == null && getIntent().getBooleanExtra(INCREMENT, false)) {
            incrementAutomatically();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        TextView tv = (TextView) currentValueTv.getCurrentView();
        savedInstanceState.putString(CURRENT_VALUE, tv.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    public void returnValue(View view) {
        int currentValue = getCurrentValue();
        SharedPreferencesUtils.saveValue(this, formId + questionId, currentValue);

        Intent intent = new Intent();
        intent.putExtra(VALUE, currentValue);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void incrementValue(View view) {
        int currentValue = getCurrentValue();
        currentValueTv.setText(String.valueOf(currentValue + 1));
        disableButtonIfNeeded(currentValue + 1);
        adjustTextSize(currentValue + 1);
    }

    public void decrementValue(View view) {
        int currentValue = getCurrentValue();
        currentValueTv.setText(String.valueOf(currentValue - 1));
        disableButtonIfNeeded(currentValue - 1);
        adjustTextSize(currentValue - 1);
    }

    private int getCurrentValue() {
        TextView tv = (TextView) currentValueTv.getCurrentView();
        return Integer.parseInt(tv.getText().toString());
    }

    private void incrementAutomatically() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int currentValue = getCurrentValue();
                if (currentValue < MAX_VALUE) {
                    currentValueTv.setText(String.valueOf(currentValue + 1));
                    disableButtonIfNeeded(currentValue + 1);
                    adjustTextSize(currentValue + 1);
                }
            }
        }, 650);
    }

    public void resetValue(View view) {
        currentValueTv.setText(getString(R.string.one));
        disableButtonIfNeeded(1);
        adjustTextSize(1);
    }

    private void disableButtonIfNeeded(int currentValue) {
        if (currentValue == MAX_VALUE) {
            plusButton.setEnabled(false);
        } else if (currentValue == MIN_VALUE) {
            minusButton.setEnabled(false);
        } else {
            plusButton.setEnabled(true);
            minusButton.setEnabled(true);
        }
    }

    private void adjustTextSize(int currentValue) {
        TextView tv = (TextView) currentValueTv.getCurrentView();
        if (currentValue > 99999 || currentValue < -9999) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);;
        } else {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);;
        }
    }

    private void setUpLayoutElements() {
        currentValueTv = (TextSwitcher) findViewById(R.id.current_value);
        currentValueTv.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView t = new TextView(CounterActivity.this);
                t.setGravity(Gravity.CENTER);
                return t;
            }
        });

        plusButton = (Button) findViewById(R.id.plus_button);
        minusButton = (Button) findViewById(R.id.minus_button);

        TextView formNameTv = (TextView) findViewById(R.id.form_name);
        TextView questionNameTv = (TextView) findViewById(R.id.question_name);

        formNameTv.setText(getIntent().getStringExtra(FORM_NAME));
        questionNameTv.setText(getIntent().getStringExtra(QUESTION_NAME));
    }

    private void setupAnimation() {
        currentValueTv.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        currentValueTv.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
    }
}

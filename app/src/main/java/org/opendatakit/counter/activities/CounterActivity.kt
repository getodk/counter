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
package org.opendatakit.counter.activities

import org.opendatakit.counter.utilities.SharedPreferencesUtils.getValue
import org.opendatakit.counter.utilities.SharedPreferencesUtils.saveValue
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.opendatakit.counter.R
import android.widget.TextView
import android.content.Intent
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.animation.AnimationUtils
import org.opendatakit.counter.databinding.ActivityCounterBinding

class CounterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCounterBinding
    private lateinit var formId: String
    private lateinit var questionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpLayout()

        formId = intent.getStringExtra(FORM_ID) ?: "formId"
        questionId = intent.getStringExtra(QUESTION_ID) ?: "questionId"

        if (savedInstanceState != null) {
            binding.currentValue.setText(savedInstanceState.getString(CURRENT_VALUE))
        } else if (getValue(this, formId + questionId) != SHARED_PREFS_DEFAULT_VALUE) {
            binding.currentValue.setText(getValue(this, formId + questionId).toString())
        } else if (intent.getBooleanExtra(INCREMENT, false)) {
            binding.currentValue.setText(getString(R.string.zero))
        } else {
            binding.currentValue.setText(getString(R.string.one))
        }

        val currentValue = getCurrentValue()
        adjustTextSize(currentValue)
        disableButtonIfNeeded(currentValue)

        if (savedInstanceState == null && intent.getBooleanExtra(INCREMENT, false)) {
            incrementAutomatically()
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        val tv = binding.currentValue.currentView as TextView
        savedInstanceState.putString(CURRENT_VALUE, tv.text.toString())
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun getCurrentValue(): Int {
        val tv = binding.currentValue.currentView as TextView
        return tv.text.toString().toInt()
    }

    private fun incrementAutomatically() {
        Handler().postDelayed({
            val currentValue = getCurrentValue()
            if (currentValue < MAX_VALUE) {
                binding.currentValue.setText((currentValue + 1).toString())
                disableButtonIfNeeded(currentValue + 1)
                adjustTextSize(currentValue + 1)
            }
        }, 650)
    }

    private fun disableButtonIfNeeded(currentValue: Int) {
        when (currentValue) {
            MAX_VALUE -> binding.plusButton.isEnabled = false
            MIN_VALUE -> binding.minusButton.isEnabled = false
            else -> {
                binding.plusButton.isEnabled = true
                binding.minusButton.isEnabled = true
            }
        }
    }

    private fun adjustTextSize(currentValue: Int) {
        val tv = binding.currentValue.currentView as TextView
        if (currentValue > 99999 || currentValue < -9999) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
        } else {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80f)
        }
    }

    private fun setUpLayout() {
        binding.currentValue.setFactory {
            TextView(this@CounterActivity).apply {
                gravity = Gravity.CENTER
            }
        }

        binding.formName.text = intent.getStringExtra(FORM_NAME)
        binding.questionName.text = intent.getStringExtra(QUESTION_NAME)

        binding.currentValue.inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.currentValue.outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)

        binding.plusButton.setOnClickListener { incrementValue() }
        binding.minusButton.setOnClickListener { decrementValue() }
        binding.resetButton.setOnClickListener { resetValue() }
        binding.returnButton.setOnClickListener { returnValue() }
    }

    private fun incrementValue() {
        val currentValue = getCurrentValue()
        binding.currentValue.setText((currentValue + 1).toString())
        disableButtonIfNeeded(currentValue + 1)
        adjustTextSize(currentValue + 1)
    }

    private fun decrementValue() {
        val currentValue = getCurrentValue()
        binding.currentValue.setText((currentValue - 1).toString())
        disableButtonIfNeeded(currentValue - 1)
        adjustTextSize(currentValue - 1)
    }

    private fun resetValue() {
        binding.currentValue.setText(getString(R.string.one))
        disableButtonIfNeeded(1)
        adjustTextSize(1)
    }

    private fun returnValue() {
        val currentValue = getCurrentValue()
        saveValue(this, formId + questionId, currentValue)
        val intent = Intent()
        intent.putExtra(VALUE, currentValue)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        // https://github.com/opendatakit/collect/blob/master/collect_app/src/main/java/org/odk/collect/android/widgets/ExIntegerWidget.java#L68
        private const val MAX_VALUE = 999999999
        private const val MIN_VALUE = -99999999
        const val SHARED_PREFS_DEFAULT_VALUE = MAX_VALUE + 1
        private const val CURRENT_VALUE = "currentValue"
        private const val FORM_ID = "form_id"
        private const val FORM_NAME = "form_name"
        private const val QUESTION_ID = "question_id"
        private const val QUESTION_NAME = "question_name"
        private const val INCREMENT = "increment"
        private const val VALUE = "value"
    }
}

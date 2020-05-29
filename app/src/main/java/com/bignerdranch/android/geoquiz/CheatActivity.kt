package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
private const val EXTRA_CHEATS_REMAINING = "com.bignerdranch.android.geoquiz.cheats_remaining"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var cheatsRemainingTextView: TextView

    private var answerIsTrue = false
    private var cheatsRemaining = 3
    private var alreadyCheated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        //Gets the values out of the EXTRAs
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        cheatsRemaining = intent.getIntExtra(EXTRA_CHEATS_REMAINING, 3)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        cheatsRemainingTextView = findViewById(R.id.cheats_remaining_text_view)

        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true) //basically to just start function below
            if (!alreadyCheated) {
                alreadyCheated = true
                --cheatsRemaining
                updateCheatsRemaining()
            }
        }

        updateCheatsRemaining()
    }

    private fun updateCheatsRemaining() {
        if (cheatsRemaining <= 0){
            cheatsRemainingTextView.setText(R.string.no_cheats_remaining)
            showAnswerButton.isEnabled = false
        }else{
            val msg = getString(R.string.cheats_remaining, cheatsRemaining)
            cheatsRemainingTextView.setText(msg)
        }

    }

    //function to set data that the user chose the show button to cheat
    private fun setAnswerShownResult(hasAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, hasAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    //used in passing data in intent, puting the data in the EXTRAs
    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, cheatsRemaining: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_CHEATS_REMAINING, cheatsRemaining)
            }
        }
    }
}
package com.example.monkey_soak_sprint

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity(), GameActivity {
    lateinit var rootLayout: LinearLayout
    lateinit var startBtn: Button
    lateinit var mGameView: GameView
    lateinit var score: TextView
    lateinit var highestScoreTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences


    //Activity life cycle onCreate method is called
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        highestScoreTextView = findViewById(R.id.highestScore)
        sharedPreferences = getSharedPreferences("GamePreferences", Context.MODE_PRIVATE)
        // Retrieve the highest score from SharedPreferences
        val highScore = sharedPreferences.getInt("highScore", 0)
        highestScoreTextView.text = "Highest Score: $highScore"

        mGameView = GameView(this, this)


        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.background4)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            score.visibility = View.GONE
        }
    }

    override fun closeGame(mScore: Int) {
        score.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE

        // Update highest score
        val highScore = sharedPreferences.getInt("highScore", 0)
        if (mScore > highScore) {
            sharedPreferences.edit().putInt("highScore", mScore).apply()
            highestScoreTextView.text = "Highest Score: $mScore"
        }

    }


}
package com.example.monkey_soak_sprint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.content.SharedPreferences

class GameView(var c :Context, var gameTask: GameActivity):View(c) {

    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var highScore = 0
    private var score = 0
    private var myMonkeyPostion = 0
    private val waterdrops = ArrayList<HashMap<String,Any>>()

    //initialize the shared preferences to store the score
    private val sharedPreferences: SharedPreferences = c.getSharedPreferences("GamePreferences", Context.MODE_PRIVATE)

    var viewWidth = 0
    var viewHeight = 0
    init {
        myPaint = Paint()

        // Retrieve the highest score from SharedPreferences
        highScore = sharedPreferences.getInt("highScore", 0)
    }


    //Calling the onDraw method to import object pictures  and game logic
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 +speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            map["isBanana"] = (0..10).random() < 2 // 20% chance of banana drop
            waterdrops.add(map)
        }

        time = time + 10 + speed
        val monkeywidth = viewWidth / 5
        val monkeyheight = monkeywidth + 10
        myPaint!!.style = Paint.Style.FILL
        //monkey object initialization
        val d = resources.getDrawable(R.drawable.monkey, null)

        d.setBounds(
            myMonkeyPostion * viewWidth / 3 + viewWidth / 15 +20,
            viewHeight-5 - monkeyheight,
            myMonkeyPostion * viewWidth / 3 + viewWidth / 15 + monkeywidth - 25,
            viewHeight - 2
        )
        d.draw(canvas)
        myPaint!!.color = Color.GREEN


        for (i in waterdrops.indices) {
            try {
                val dropX = waterdrops[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var dropY = time - waterdrops[i]["startTime"] as Int
                //water drops, banana objects initialization
                val drawableId = if (waterdrops[i]["isBanana"] as Boolean) R.drawable.banana else R.drawable.water
                val drawable = resources.getDrawable(drawableId, null)

                //positioning the water drops and bananas

                drawable.setBounds(
                    dropX + 25, dropY - monkeyheight , dropX + monkeywidth - 25 , dropY
                )
                drawable.draw(canvas)
                if (waterdrops[i]["lane"] as Int == myMonkeyPostion) {
                    if (dropY > viewHeight - 2 - monkeyheight && dropY < viewHeight - 2) {
                        if (waterdrops[i]["isBanana"] as Boolean) {
                            score += 2 // Increment score by 2 if it's a banana
                        } else {
                            gameTask.closeGame(score)
                        }
                    }
                }

                if (dropY > viewHeight + monkeyheight) {
                    waterdrops.removeAt(i)
                    if (waterdrops[i]["isBanana"] as Boolean) {
                        score++
                    }
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if (score > highScore) {
                        highScore = score
                    }
                }
            } catch (e:Exception) {
                e.printStackTrace()
            }
            // Update high score if necessary
            if (score > highScore) {
                highScore = score
                // Save the new high score to SharedPreferences
                sharedPreferences.edit().putInt("highScore", highScore).apply()
            }
        }

        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f,myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f,myPaint!!)
        //redraw of the view. This ensures that
        //any changes made to the game state, such as resetting the score, are reflected on
        //the screen
        invalidate()
    }


    //Handle the monkey object movements (Touch Event)
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myMonkeyPostion > 0) {
                        myMonkeyPostion--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myMonkeyPostion < 2) {
                        myMonkeyPostion++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {

            }
        }

        return true
    }
}
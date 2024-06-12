package com.first.mathsgame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Locale
import kotlin.random.Random
import android.os.CountDownTimer

class GameActivity : AppCompatActivity() {
    lateinit var actionTitle : String
    lateinit var textScore : TextView
    lateinit var textLife : TextView
    lateinit var textTime : TextView
    private lateinit var buttonOk : Button
    lateinit var buttonNext : Button
    lateinit var textQuestion : TextView
    private lateinit var textAnswer : EditText
    var correctAnswer = 0
    var userScore = 0
    var userLife = 3
     lateinit var timer : CountDownTimer
    private val startTimerInMillis : Long = 20000
    var timeLeftInMillis : Long = startTimerInMillis
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameactivity)
        actionTitle = intent.getStringExtra("actionTitle").toString()

        when(actionTitle){
            "Addition" -> supportActionBar!!.title = "Addition"
            "Subtraction" -> supportActionBar!!.title = "Subtraction"
            "Multiplication" -> supportActionBar!!.title = "Multiplication"
        }
        textScore = findViewById(R.id.textViewScore)
        textLife = findViewById(R.id.textViewLife)
        textTime = findViewById(R.id.textViewTime)
        buttonOk = findViewById(R.id.buttonOk)
        buttonNext = findViewById(R.id.buttonNext)
         textQuestion = findViewById(R.id.textViewQuestion)
        textAnswer = findViewById(R.id.editTextNumberAnswer)
        gameContinue()
        buttonOk.setOnClickListener {
            val input = textAnswer.text.toString()
            if(input==""){
                Toast.makeText(applicationContext,"Please write answer or click on next button",Toast.LENGTH_LONG).show()
            }
            else{
                buttonOk.isClickable = false
                pauseTimer()
                val userAnswer = input.toInt()
                if(userAnswer==correctAnswer){
                    userScore += 10
                    textQuestion.text = "Congratulations,You have Entered the correct answer"
                    textScore.text = userScore.toString()
                }
                else{
                    userLife--
                    textQuestion.text = "Sorry,You have entered the wrong answer"
                    textLife.text = userLife.toString()
                }
            }
        }
        buttonNext.setOnClickListener {
            textAnswer.setText("")
            buttonOk.isClickable = true
            pauseTimer()
            resetTimer()
            if(userLife==0){
                Toast.makeText(applicationContext,"Game Over",Toast.LENGTH_LONG).show()
                val intent = Intent(this@GameActivity,ResultActivity::class.java)
                intent.putExtra("score",userScore)
                startActivity(intent)
                finish()
            }
            else{
                gameContinue()
            }
        }

    }
    private fun gameContinue(){
        var number1 = Random.nextInt(0,100)
        var number2 = Random.nextInt(0,100)
        if(actionTitle == "Addition")
        {
            textQuestion.text = "$number1 + $number2"

            correctAnswer = number1 + number2
        }
        else if (actionTitle == "Subtraction")
        {
            if (number1 >= number2)
            {
                textQuestion.text = "$number1 - $number2"

                correctAnswer = number1 - number2
            }
            else
            {
                textQuestion.text = "$number2 - $number1"

                correctAnswer = number2 - number1
            }
        }
        else
        {
            val number3 = Random.nextInt(0,20)
            val number4 = Random.nextInt(0,20)

            textQuestion.text = "$number3 * $number4"

            correctAnswer = number3 * number4
        }
        startTimer()
    }
     fun startTimer(){
        timer = object : CountDownTimer(timeLeftInMillis,1000){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateText()

            }
           override fun onFinish() {
                pauseTimer()
                resetTimer()
                updateText()
                userLife--
                textLife.text = userLife.toString()
                textQuestion.text = "Sorry your time is up!!"
            }

        }.start()
    }
    fun updateText(){
        val remainingTime : Int = (timeLeftInMillis/1000).toInt()
        textTime.text = String.format(Locale.getDefault(),"%02d",remainingTime)
    }
    fun pauseTimer(){
        timer.cancel()
    }
    fun resetTimer(){
        timeLeftInMillis = startTimerInMillis
        updateText()
    }
}
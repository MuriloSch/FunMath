package com.dq.funmath

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dq.funmath.models.GameResult
import com.dq.funmath.models.Question
import kotlin.random.Random

class LargestNumberActivity : AppCompatActivity() {

    private lateinit var tvDigits: TextView
    private lateinit var etAnswer: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvQuestionCount: TextView

    private val totalQuestions = 5
    private var currentQuestion = 0
    private var correctAnswers = 0
    private var currentDigits = listOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_largest_number)

        initializeViews()
        setupGame()
    }

    private fun initializeViews() {
        tvDigits = findViewById(R.id.tvDigits)
        etAnswer = findViewById(R.id.etAnswer)
        btnSubmit = findViewById(R.id.btnSubmit)
        tvQuestionCount = findViewById(R.id.tvQuestionCount)

        btnSubmit.setOnClickListener {
            checkAnswer()
        }
    }

    private fun setupGame() {
        currentQuestion = 0
        correctAnswers = 0
        showNextQuestion()
    }

    private fun showNextQuestion() {
        if (currentQuestion >= totalQuestions) {
            showFinalResult()
            return
        }

        currentQuestion++
        tvQuestionCount.text = "Pergunta $currentQuestion/$totalQuestions"
        etAnswer.text.clear()

        val question = generateQuestion()
        displayQuestion(question)
    }

    private fun generateQuestion(): Question {
        currentDigits = List(3) { Random.nextInt(0, 10) }
        val correctAnswer = currentDigits.sortedDescending().joinToString("").toInt()

        return Question(
            digits = currentDigits,
            correctAnswer = correctAnswer
        )
    }

    private fun displayQuestion(question: Question) {
        tvDigits.text = "Dígitos: ${currentDigits.joinToString(", ")}"
    }

    private fun checkAnswer() {
        val userAnswer = etAnswer.text.toString().toIntOrNull()
        val correctAnswer = currentDigits.sortedDescending().joinToString("").toInt()

        if (userAnswer == correctAnswer) {
            correctAnswers++
            showResultDialog(true, correctAnswer)
        } else {
            showResultDialog(false, correctAnswer)
        }
    }

    private fun showResultDialog(isCorrect: Boolean, correctAnswer: Int) {
        val title = if (isCorrect) "Excelente!" else "Ops!"
        val message = if (isCorrect)
            "Resposta correta! O maior número é $correctAnswer"
        else
            "Resposta incorreta. O maior número possível é $correctAnswer."

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Próxima") { _, _ ->
                showNextQuestion()
            }
            .setCancelable(false)
            .show()
    }

    private fun showFinalResult() {
        val result = GameResult(totalQuestions, correctAnswers,
            (correctAnswers * 100) / totalQuestions)

        AlertDialog.Builder(this)
            .setTitle("Fim do Jogo!")
            .setMessage("Sua pontuação: ${result.getPercentage()}%\n" +
                    "Acertos: $correctAnswers/$totalQuestions")
            .setPositiveButton("Jogar Novamente") { _, _ ->
                setupGame()
            }
            .setNegativeButton("Voltar ao Menu") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
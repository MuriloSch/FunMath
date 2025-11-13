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

class ArithmeticGameActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var etAnswer: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvQuestionCount: TextView

    private val totalQuestions = 5
    private var currentQuestion = 0
    private var correctAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arithimetic)

        initializeViews()
        setupGame()
    }

    private fun initializeViews() {
        tvExpression = findViewById(R.id.tvExpression)
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
        val operand1 = Random.nextInt(0, 10)
        val operand2 = Random.nextInt(0, 10)
        val operator = if (Random.nextBoolean()) "+" else "-"

        val expression = "$operand1 $operator $operand2"
        val correctAnswer = if (operator == "+") operand1 + operand2 else operand1 - operand2

        return Question(
            arithmeticExpression = expression,
            correctAnswer = correctAnswer
        )
    }

    private fun displayQuestion(question: Question) {
        tvExpression.text = question.arithmeticExpression
    }

    private fun checkAnswer() {
        val userAnswer = etAnswer.text.toString().toIntOrNull()
        val correctAnswer = getCurrentCorrectAnswer()

        if (userAnswer == correctAnswer) {
            correctAnswers++
            showResultDialog(true, correctAnswer)
        } else {
            showResultDialog(false, correctAnswer)
        }
    }

    private fun getCurrentCorrectAnswer(): Int {
        // Implementar lógica para calcular a resposta correta baseada na expressão atual
        val expression = tvExpression.text.toString()
        val parts = expression.split(" ")
        val operand1 = parts[0].toInt()
        val operand2 = parts[2].toInt()
        return if (parts[1] == "+") operand1 + operand2 else operand1 - operand2
    }

    private fun showResultDialog(isCorrect: Boolean, correctAnswer: Int) {
        val title = if (isCorrect) "Correto!" else "Incorreto"
        val message = if (isCorrect)
            "Resposta correta!"
        else
            "Resposta incorreta. A resposta correta era $correctAnswer."

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
package com.dq.funmath

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.dq.funmath.models.Question
import com.dq.funmath.models.GameResult
import kotlin.random.Random

class CountingGameActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnOption1: Button
    private lateinit var btnOption2: Button
    private lateinit var btnOption3: Button
    private lateinit var tvQuestionCount: TextView

    private val totalQuestions = 5
    private var currentQuestion = 0
    private var correctAnswers = 0
    private var usedImages = mutableSetOf<Int>()

    // Imagens e suas quantidades correspondentes
    private val imageResources = listOf(
        Pair(R.drawable.counting1, 1),
        Pair(R.drawable.counting2, 2),
        Pair(R.drawable.counting3, 3),
        Pair(R.drawable.counting4, 4),
        Pair(R.drawable.counting5, 5),
        Pair(R.drawable.counting6, 6),
        Pair(R.drawable.counting7, 7),
        Pair(R.drawable.counting8, 8),
        Pair(R.drawable.counting9, 9),
        Pair(R.drawable.counting10, 10)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_couting)

        initializeViews()
        setupGame()
    }

    private fun initializeViews() {
        imageView = findViewById(R.id.imageView)
        btnOption1 = findViewById(R.id.btnOption1)
        btnOption2 = findViewById(R.id.btnOption2)
        btnOption3 = findViewById(R.id.btnOption3)
        tvQuestionCount = findViewById(R.id.tvQuestionCount)
    }

    private fun setupGame() {
        usedImages.clear()
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

        val question = generateQuestion()
        displayQuestion(question)
    }

    private fun generateQuestion(): Question {
        // Seleciona uma imagem não utilizada
        val availableImages = imageResources.filter { !usedImages.contains(it.first) }
        val selectedImage = availableImages.random()
        usedImages.add(selectedImage.first)

        val correctAnswer = selectedImage.second
        val options = generateOptions(correctAnswer)

        return Question(
            imageResId = selectedImage.first,
            correctAnswer = correctAnswer,
            options = options
        )
    }

    private fun generateOptions(correctAnswer: Int): List<Int> {
        val options = mutableListOf(correctAnswer)

        while (options.size < 3) {
            val randomOption = Random.nextInt(1, 11)
            if (randomOption != correctAnswer && !options.contains(randomOption)) {
                options.add(randomOption)
            }
        }

        return options.shuffled()
    }

    private fun displayQuestion(question: Question) {
        question.imageResId?.let { imageView.setImageResource(it) }

        val options = question.options ?: emptyList()
        if (options.size >= 3) {
            btnOption1.text = options[0].toString()
            btnOption2.text = options[1].toString()
            btnOption3.text = options[2].toString()

            btnOption1.setOnClickListener { checkAnswer(options[0], question.correctAnswer) }
            btnOption2.setOnClickListener { checkAnswer(options[1], question.correctAnswer) }
            btnOption3.setOnClickListener { checkAnswer(options[2], question.correctAnswer) }
        }
    }

    private fun checkAnswer(selectedAnswer: Int, correctAnswer: Int) {
        if (selectedAnswer == correctAnswer) {
            correctAnswers++
            showResultDialog(true, correctAnswer)
        } else {
            showResultDialog(false, correctAnswer)
        }
    }

    private fun showResultDialog(isCorrect: Boolean, correctAnswer: Int) {
        val title = if (isCorrect) "Parabéns!" else "Tente Novamente"
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
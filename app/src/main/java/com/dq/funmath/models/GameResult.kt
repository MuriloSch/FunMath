package com.dq.funmath.models

data class GameResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val score: Int
) {
    fun getPercentage(): Int = (correctAnswers * 100) / totalQuestions
}
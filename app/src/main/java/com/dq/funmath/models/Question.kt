package com.dq.funmath.models

data class Question(
    val imageResId: Int? = null,
    val correctAnswer: Int,
    val options: List<Int>? = null,
    val arithmeticExpression: String? = null,
    val digits: List<Int>? = null
)
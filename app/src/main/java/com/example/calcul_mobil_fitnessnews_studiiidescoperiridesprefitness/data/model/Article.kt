package com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.model

data class Article(
    val id: Int,
    val title: String,
    val summary: String,
    val content: String,
    val category: String,
    val author: String,
    val date: String,
    val readTime: String,
    val imageUrl: String,
    val sourceUrl: String,
    val isBookmarked: Boolean = false
)

package com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.model.Article

@Entity(tableName = "favorite_articles")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val summary: String,
    val content: String,
    val category: String,
    val author: String,
    val date: String,
    val readTime: String,
    val imageUrl: String,
    val sourceUrl: String
) {
    fun toArticle(): Article {
        return Article(
            id = id,
            title = title,
            summary = summary,
            content = content,
            category = category,
            author = author,
            date = date,
            readTime = readTime,
            imageUrl = imageUrl,
            sourceUrl = sourceUrl,
            isBookmarked = true
        )
    }

    companion object {
        fun fromArticle(article: Article): FavoriteEntity {
            return FavoriteEntity(
                id = article.id,
                title = article.title,
                summary = article.summary,
                content = article.content,
                category = article.category,
                author = article.author,
                date = article.date,
                readTime = article.readTime,
                imageUrl = article.imageUrl,
                sourceUrl = article.sourceUrl
            )
        }
    }
}

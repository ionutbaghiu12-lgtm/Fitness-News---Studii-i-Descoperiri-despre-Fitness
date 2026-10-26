package com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.api

import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.model.Article
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class FitnessArticleDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("content") val content: String,
    @SerializedName("category") val category: String,
    @SerializedName("author") val author: String,
    @SerializedName("date") val date: String,
    @SerializedName("readTime") val readTime: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("sourceUrl") val sourceUrl: String
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
            sourceUrl = sourceUrl
        )
    }
}

interface FitnessApiService {
    @GET("fitness_studies.json")
    suspend fun getArticles(): List<FitnessArticleDto>

    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com/ionutbaghiu12-lgtm/Fitness-News---Studii-i-Descoperiri-despre-Fitness/main/"

        fun create(): FitnessApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FitnessApiService::class.java)
        }
    }
}

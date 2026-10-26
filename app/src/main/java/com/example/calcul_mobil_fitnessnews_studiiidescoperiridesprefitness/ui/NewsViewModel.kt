package com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.model.Article
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: ArticleRepository
) : ViewModel() {

    private val _rawArticles = MutableStateFlow<List<Article>>(emptyList())

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("Toate")
    val isLoading = MutableStateFlow(true)

    val favoriteArticles: StateFlow<List<Article>> = repository.favoriteArticles.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val categories = listOf("Toate", "Hipertrofie", "Nutriție", "Cardio", "Suplimente", "Recuperare")

    val filteredArticles: StateFlow<List<Article>> = combine(
        _rawArticles,
        repository.favoriteIds,
        searchQuery,
        selectedCategory
    ) { raw, favIds, query, category ->
        raw.map { article ->
            article.copy(isBookmarked = favIds.contains(article.id))
        }.filter { article ->
            val matchesCategory = (category == "Toate") || article.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isEmpty() ||
                    article.title.contains(query, ignoreCase = true) ||
                    article.summary.contains(query, ignoreCase = true) ||
                    article.author.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            isLoading.value = true
            _rawArticles.value = repository.fetchArticles()
            isLoading.value = false
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun selectCategory(category: String) {
        selectedCategory.value = category
    }

    fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            repository.toggleBookmark(article)
        }
    }

    fun getArticleById(id: Int): Article? {
        val currentFavs = favoriteArticles.value
        val inRaw = _rawArticles.value.find { it.id == id }
        val inFav = currentFavs.find { it.id == id }
        val found = inRaw ?: inFav
        return found?.copy(isBookmarked = currentFavs.any { it.id == id })
    }

    class Factory(private val repository: ArticleRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                return NewsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

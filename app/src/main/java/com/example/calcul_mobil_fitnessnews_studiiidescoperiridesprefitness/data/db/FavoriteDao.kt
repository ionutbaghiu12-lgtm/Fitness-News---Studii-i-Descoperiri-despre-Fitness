package com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteDao(context: Context) : SQLiteOpenHelper(context, "fitness_news_database.db", null, 1) {

    private val _favoritesFlow = MutableStateFlow<List<FavoriteEntity>>(emptyList())
    val favoritesFlow: StateFlow<List<FavoriteEntity>> = _favoritesFlow.asStateFlow()

    init {
        refreshFavorites()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE favorite_articles (
                id INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                summary TEXT NOT NULL,
                content TEXT NOT NULL,
                category TEXT NOT NULL,
                author TEXT NOT NULL,
                date TEXT NOT NULL,
                readTime TEXT NOT NULL,
                imageUrl TEXT NOT NULL,
                sourceUrl TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS favorite_articles")
        onCreate(db)
    }

    private fun refreshFavorites() {
        val list = mutableListOf<FavoriteEntity>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM favorite_articles", null)
        cursor.use { c ->
            while (c.moveToNext()) {
                list.add(
                    FavoriteEntity(
                        id = c.getInt(c.getColumnIndexOrThrow("id")),
                        title = c.getString(c.getColumnIndexOrThrow("title")),
                        summary = c.getString(c.getColumnIndexOrThrow("summary")),
                        content = c.getString(c.getColumnIndexOrThrow("content")),
                        category = c.getString(c.getColumnIndexOrThrow("category")),
                        author = c.getString(c.getColumnIndexOrThrow("author")),
                        date = c.getString(c.getColumnIndexOrThrow("date")),
                        readTime = c.getString(c.getColumnIndexOrThrow("readTime")),
                        imageUrl = c.getString(c.getColumnIndexOrThrow("imageUrl")),
                        sourceUrl = c.getString(c.getColumnIndexOrThrow("sourceUrl"))
                    )
                )
            }
        }
        _favoritesFlow.value = list
    }

    fun getAllFavorites(): StateFlow<List<FavoriteEntity>> = favoritesFlow

    fun getFavoriteIds(): List<Int> {
        return _favoritesFlow.value.map { it.id }
    }

    fun insertFavorite(favorite: FavoriteEntity) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", favorite.id)
            put("title", favorite.title)
            put("summary", favorite.summary)
            put("content", favorite.content)
            put("category", favorite.category)
            put("author", favorite.author)
            put("date", favorite.date)
            put("readTime", favorite.readTime)
            put("imageUrl", favorite.imageUrl)
            put("sourceUrl", favorite.sourceUrl)
        }
        db.insertWithOnConflict("favorite_articles", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        refreshFavorites()
    }

    fun deleteFavoriteById(id: Int) {
        val db = writableDatabase
        db.delete("favorite_articles", "id = ?", arrayOf(id.toString()))
        refreshFavorites()
    }
}

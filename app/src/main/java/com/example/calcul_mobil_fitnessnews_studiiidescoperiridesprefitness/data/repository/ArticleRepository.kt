package com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.repository

import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.api.FitnessApiService
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.db.FavoriteDao
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.db.FavoriteEntity
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepository(
    private val apiService: FitnessApiService,
    private val favoriteDao: FavoriteDao
) {
    val favoriteArticles: Flow<List<Article>> = favoriteDao.getAllFavorites().map { entities ->
        entities.map { it.toArticle() }
    }

    val favoriteIds: Flow<List<Int>> = favoriteDao.getFavoriteIds()

    suspend fun fetchArticles(): List<Article> {
        return try {
            val dtos = apiService.getArticles()
            dtos.map { it.toArticle() }
        } catch (e: Exception) {
            getInitialSampleArticles()
        }
    }

    suspend fun toggleBookmark(article: Article) {
        if (article.isBookmarked) {
            favoriteDao.deleteFavoriteById(article.id)
        } else {
            favoriteDao.insertFavorite(FavoriteEntity.fromArticle(article))
        }
    }

    private fun getInitialSampleArticles(): List<Article> {
        return listOf(
            Article(
                id = 1,
                title = "Efectul volumului de antrenament asupra hipertrofiei musculare",
                summary = "Un nou studiu meta-analitic arată că un volum între 10 și 20 de serii pe grupă musculară pe săptămână optimizează creșterea musculară.",
                content = "Cercetătorii au analizat 15 studii clinice derulate pe atleți cu experiență. Rezultatele demonstrează o relație doză-răspuns clară: volumele de 10-20 serii per grupă musculară pe săptămână determină o rată cu 38% mai mare de sinteză proteică musculară comparativ cu volumele sub 10 serii.\n\n" +
                        "Recomandări practice:\n" +
                        "1. Începeți cu 10-12 serii săptămânale per grupă musculară.\n" +
                        "2. Creșteți progresiv volumul la un interval de 4-6 săptămâni.\n" +
                        "3. Monitorizați recuperarea și calitatea somnului.",
                category = "Hipertrofie",
                author = "Dr. Brad Schoenfeld",
                date = "24 Februarie 2026",
                readTime = "4 min",
                imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=800",
                sourceUrl = "https://journalofstrengthandconditioning.com"
            ),
            Article(
                id = 2,
                title = "Timpul optim de ingestie a proteinelor post-antrenament",
                summary = "Fereastra anabolică este mai largă decât se credea inițial: ingestia de proteine în primele 2 ore oferă stimul maxim.",
                content = "Mitul conform căruia trebuie să consumi un shake proteic în primele 30 de minute după antrenament a fost infirmat. Studiile recente de izotopi stabili indică faptul că fereastra de sensibilitate la aminoacizi rămâne ridicată până la 2-3 ore după terminarea efortului fizic.\n\n" +
                        "Puncte cheie:\n" +
                        "• Consumați 0.4-0.5g proteine per kg corp per masă.\n" +
                        "• Rata totală zilnică (1.6 - 2.2g/kg corp) rămâne cel mai important factor.",
                category = "Nutriție",
                author = "Dr. Alan Aragon",
                date = "20 Februarie 2026",
                readTime = "5 min",
                imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?q=80&w=800",
                sourceUrl = "https://jissn.biomedcentral.com"
            ),
            Article(
                id = 3,
                title = "Zone 2 Cardio și longevitatea mitocondrială",
                summary = "Antrenamentul aerobic la intensitate moderată îmbunătățește densitatea mitocondrială și eficiența metabolică.",
                content = "Măsurătorile de lactat sanguin au dovedit că antrenamentul regulat în Zona 2 (menținerea lactatului între 1.5 - 2.0 mmol/L) stimulează biogeneza mitocondrială și oxidarea acizilor grași.\n\n" +
                        "Beneficii pe termen lung:\n" +
                        "• Scăderea rezistenței la insulină.\n" +
                        "• Îmbunătățirea capacității de recuperare între seriile grele de forță.",
                category = "Cardio",
                author = "Dr. Inigo San Millan",
                date = "15 Februarie 2026",
                readTime = "6 min",
                imageUrl = "https://images.unsplash.com/photo-1483721063318-77b02927d6d5?q=80&w=800",
                sourceUrl = "https://medicineandscienceinsports.com"
            ),
            Article(
                id = 4,
                title = "Creatina Monohidrat: Impactul asupra performanței cognitive și musculare",
                summary = "Pe lângă creșterea forței explozive, suplimentarea cu creatină susține funcțiile executive ale creierului în condiții de privare de somn.",
                content = "Creatina rămâne cel mai studiat supliment din faza modernă a științei sportului. Cercetări noi din 2025/2026 arată că rezervele de fosfocreatină din cortexul prefrontal ajută la reducerea oboselii mentale în timpul antrenamentelor intense de seară.",
                category = "Suplimente",
                author = "Dr. Eric Trexler",
                date = "10 Februarie 2026",
                readTime = "3 min",
                imageUrl = "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?q=80&w=800",
                sourceUrl = "https://examine.com"
            ),
            Article(
                id = 5,
                title = "Rolul somnului REM și NREM în sinteza proteică și recuperarea musculară",
                summary = "Lipsa somnului profund scade nivelul de GH (Hormon de Creștere) cu până la 70%, afectând refacerea țesuturilor.",
                content = "Analiza polisomnografică pe atleți arată că stadiul NREM 3 (somn profund cu unde lente) este perioada principală în care hipofiza eliberează hormon de creștere. Reducerea somnului la 5-6 ore pe noapte duce la o pierdere accelerată a masei musculare în timpul deficitului caloric.",
                category = "Recuperare",
                author = "Dr. Matthew Walker",
                date = "5 Februarie 2026",
                readTime = "4 min",
                imageUrl = "https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?q=80&w=800",
                sourceUrl = "https://sleepfoundation.org"
            )
        )
    }
}

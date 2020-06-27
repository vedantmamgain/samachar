package dev.vedantm.samachar.db

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.vedantm.samachar.models.Article


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //basically save ya update
    suspend fun upsert(article: Article):Long //returning the long of rthe id pf the id inserted

    @Query("SELECT * FROM articles")
    fun  getAllArticles():LiveData<List<Article>>

    @Delete
    suspend fun deletedArticle(article: Article)



}
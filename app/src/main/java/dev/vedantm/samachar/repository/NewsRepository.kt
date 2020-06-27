package dev.vedantm.samachar.repository

import dev.vedantm.samachar.api.RetroFitInstance
import dev.vedantm.samachar.db.ArticleDatabase
import dev.vedantm.samachar.models.Article

class NewsRepository (
    val db:ArticleDatabase
){
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) = RetroFitInstance.api.getBreakingNews(countryCode,pageNumber)
    suspend fun searchNews(searchQuery:String,pageNumber:Int) = RetroFitInstance.api.searchForNews(searchQuery,pageNumber)
suspend fun upsert(article:Article) = db.getArticleDao().upsert(article)
    fun getSavedNews() = db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article) =db.getArticleDao().deletedArticle(article)
}
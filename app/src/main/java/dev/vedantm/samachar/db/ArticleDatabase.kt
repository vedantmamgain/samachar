package dev.vedantm.samachar.db

import android.content.Context
import androidx.room.*
import dev.vedantm.samachar.models.Article

@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class ArticleDatabase :RoomDatabase(){

    abstract fun getArticleDao():ArticleDao

    companion object{
        @Volatile //Other threads can see when this instance changes
        private var instance:ArticleDatabase?=null
        private var LOCK =Any() //only a single instanace at once

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK) {
             instance ?:createDatabase(context).also{
                 instance=it
             }
        }//such that no other thread can use this db

        private fun createDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
        ArticleDatabase::class.java,"article_db.db").build()
    }

}
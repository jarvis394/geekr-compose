package com.jarvis394.geekr.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jarvis394.geekr.data.model.PersonalArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalArticlesDao {
    @Query("SELECT * FROM personal_articles ORDER BY createdAt DESC")
    fun getAllArticles(): Flow<List<PersonalArticle>>

    @Query("SELECT * FROM personal_articles WHERE id = :id")
    suspend fun getArticleById(id: Long): PersonalArticle?

    @Query("SELECT * FROM personal_articles WHERE id = :id")
    fun getArticleStreamById(id: Long): Flow<PersonalArticle?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: PersonalArticle): Long

    @Query("DELETE FROM personal_articles WHERE id = :id")
    suspend fun deleteArticle(id: Long)
}

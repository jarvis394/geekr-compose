package com.jarvis394.geekr.data.repository

import com.jarvis394.geekr.data.dao.PersonalArticlesDao
import com.jarvis394.geekr.data.model.PersonalArticle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalArticlesRepository @Inject constructor(
    private val personalArticlesDao: PersonalArticlesDao
) {
    fun getAllArticles(): Flow<List<PersonalArticle>> {
        return personalArticlesDao.getAllArticles()
    }

    suspend fun getArticleById(id: Long): PersonalArticle? {
        return personalArticlesDao.getArticleById(id)
    }

    fun getArticleStreamById(id: Long): Flow<PersonalArticle?> {
        return personalArticlesDao.getArticleStreamById(id)
    }

    suspend fun saveArticle(title: String, content: String): Long {
        val article = PersonalArticle(
            title = title,
            content = content
        )
        return personalArticlesDao.insertArticle(article)
    }

    suspend fun updateArticle(id: Long, title: String, content: String) {
        val existing = personalArticlesDao.getArticleById(id)
        if (existing != null) {
            val updated = existing.copy(title = title, content = content)
            personalArticlesDao.insertArticle(updated)
        }
    }

    suspend fun deleteArticle(id: Long) {
        personalArticlesDao.deleteArticle(id)
    }
}

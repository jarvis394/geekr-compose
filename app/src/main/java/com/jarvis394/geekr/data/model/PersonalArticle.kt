package com.jarvis394.geekr.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personal_articles")
data class PersonalArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)

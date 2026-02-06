package com.jarvis394.geekr.ui.profile

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM users WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Upsert
    suspend fun upsert(user: UserProfile)
}
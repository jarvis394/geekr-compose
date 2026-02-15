package com.jarvis394.geekr.data.repository

import com.jarvis394.geekr.ui.profile.UserProfile
import com.jarvis394.geekr.ui.profile.UserProfileDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {
    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()

    suspend fun updateProfile(alias: String, avatarUrl: String? = null, fullname: String? = null) {
        userProfileDao.upsert(
            UserProfile(alias = alias, avatarUrl = avatarUrl, fullname = fullname)
        )
    }

    suspend fun deleteProfile() {
        userProfileDao.delete()
    }
}
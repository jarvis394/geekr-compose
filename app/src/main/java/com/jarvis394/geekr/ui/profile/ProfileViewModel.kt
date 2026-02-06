package com.jarvis394.geekr.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userProfileDao: UserProfileDao) :
    ViewModel() {
    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()

    fun updateProfile(alias: String, avatarUrl: String? = null, fullname: String? = null) {
        viewModelScope.launch {
            userProfileDao.upsert(
                UserProfile(alias = alias, avatarUrl = avatarUrl, fullname = fullname)
            )
        }
    }
}
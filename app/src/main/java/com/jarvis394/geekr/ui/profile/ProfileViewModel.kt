package com.jarvis394.geekr.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis394.geekr.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    val userProfile: Flow<UserProfile?> = userRepository.userProfile

    fun updateProfile(alias: String, avatarUrl: String? = null, fullname: String? = null) {
        viewModelScope.launch {
            userRepository.updateProfile(alias, avatarUrl, fullname)
        }
    }

    fun deleteProfile() {
        viewModelScope.launch {
            userRepository.deleteProfile()
        }
    }
}
package com.jarvis394.geekr.ui.newArticle

import ArticleLabel
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis394.geekr.data.model.Article
import com.jarvis394.geekr.data.model.ArticleComplexity
import com.jarvis394.geekr.data.model.ArticleFormat
import com.jarvis394.geekr.data.model.ArticleMetadata
import com.jarvis394.geekr.data.model.ArticleStatistics
import com.jarvis394.geekr.data.model.CommentsEnabled
import com.jarvis394.geekr.data.model.Flow
import com.jarvis394.geekr.data.model.HubArticleLabels
import com.jarvis394.geekr.data.model.LeadData
import com.jarvis394.geekr.data.model.PostType
import com.jarvis394.geekr.data.model.Profile
import com.jarvis394.geekr.data.model.Tag
import com.jarvis394.geekr.data.model.TranslationData
import com.jarvis394.geekr.data.remote.HabrApiService
import com.jarvis394.geekr.data.repository.ArticlesRepository
import com.jarvis394.geekr.data.repository.PersonalArticlesRepository
import com.jarvis394.geekr.util.ArticleNotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import kotlin.String

@HiltViewModel()
class NewArticleViewModel @Inject constructor(
    private val personalArticlesRepository: PersonalArticlesRepository,
    private val notificationService: ArticleNotificationService
) : ViewModel() {
    var uiState by mutableStateOf<NewArticleUIState>(NewArticleUIState.Loading)
        private set

    // fun uploadImage(uri: Uri, onUploadSuccess: (String) -> Unit) {
    //     viewModelScope.launch(Dispatchers.IO) {
    //         try {
    //             val contentResolver = context.contentResolver
    //             val inputStream = contentResolver.openInputStream(uri) ?: return@launch
    //             val mimeType = contentResolver.getType(uri) ?: "image/jpeg"

    //             val requestFile =
    //                 inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())
    //             val body = MultipartBody.Part.createFormData("file", "upload.jpg", requestFile)
    //             val typePart = "direct".toRequestBody("text/plain".toMediaType())
    //             val response = habrApi.uploadImage(body, typePart)

    //             withContext(Dispatchers.Main) {
    //                 response.url?.let { onUploadSuccess(it) }
    //             }
    //         } catch (e: Exception) {
    //             // Log error or update UI state with error message
    //         }
    //     }
    // }

    fun loadPersonalArticle(id: Long, onLoaded: (String, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val article = personalArticlesRepository.getArticleById(id)
            if (article != null) {
                withContext(Dispatchers.Main) {
                    onLoaded(article.title, article.content)
                }
            }
        }
    }

    fun savePersonalArticle(title: String, content: String, existingId: Long? = null, onComplete: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = if (existingId != null) {
                personalArticlesRepository.updateArticle(existingId, title, content)
                existingId
            } else {
                val newId = personalArticlesRepository.saveArticle(title, content)
                notificationService.showArticleReadyNotification(title, newId)
                newId
            }
            withContext(Dispatchers.Main) {
                onComplete(id)
            }
        }
    }
}

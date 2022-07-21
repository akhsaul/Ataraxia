package com.gkkendor.app.ui.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkkendor.app.model.Article
import com.gkkendor.app.model.ArticleResponse
import com.gkkendor.app.util.Constants
import com.gkkendor.app.util.Resource
import com.gkkendor.app.util.formatTo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ArticleViewModel : ViewModel() {

    val articles: MutableLiveData<Resource<ArticleResponse>> = MutableLiveData()

    init {
        getArticles()
    }

    fun getArticles() = viewModelScope.launch {
        articles.postValue(Resource.Loading())
        try {
            val sdf = SimpleDateFormat(Constants.PATTERN_DATE)
            val articleList = mutableListOf<Article>().apply {
                repeat(25) {
                    val id = it + 1
                    add(
                        Article(
                            id,
                            title = "Tittle $id",
                            content = Constants.randomString(255 * 2),
                            publishedAt = LocalDateTime.now().toString().formatTo(sdf)
                        )
                    )
                }
            }
            articles.postValue(
                Resource.Success(
                    ArticleResponse(
                        articleList,
                        "success",
                        articleList.size
                    )
                )
            )
        } catch (t: Throwable) {
            articles.postValue(Resource.Error(t.message ?: "Unknown", null))
        }
    }
}
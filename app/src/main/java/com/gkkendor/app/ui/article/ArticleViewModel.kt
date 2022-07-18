package com.gkkendor.app.ui.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkkendor.app.model.Article
import com.gkkendor.app.model.ArticleResponse
import com.gkkendor.app.util.Constants
import com.gkkendor.app.util.Resource
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ArticleViewModel : ViewModel() {

    val articles: MutableLiveData<Resource<ArticleResponse>> = MutableLiveData()

    init {
        getArticles()
    }

    fun getArticles() = viewModelScope.launch {
        articles.postValue(Resource.Loading())
        try {
            val articleList = mutableListOf<Article>().apply {
                repeat(25) {
                    val id = it + 1
                    add(
                        Article(
                            id,
                            title = "Tittle $id",
                            description = Constants.randomString(255 * 2),
                            publishedAt = "${LocalDateTime.now()}"
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
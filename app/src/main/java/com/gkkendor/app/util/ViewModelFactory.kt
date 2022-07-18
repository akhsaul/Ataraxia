package com.gkkendor.app.util


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.gkkendor.app.ui.home.HomeViewModel
//import com.gkkendor.app.ui.news.NewsViewModel
import com.gkkendor.app.ui.report.ReportViewModel

class ReportViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            ReportViewModel() as T
        } else {
            throw IllegalStateException()
        }
    }
}

/*
class NewsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            NewsViewModel() as T
        } else {
            throw IllegalStateException()
        }
    }
}*/

class HomeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel() as T
        } else {
            throw IllegalStateException()
        }
    }
}
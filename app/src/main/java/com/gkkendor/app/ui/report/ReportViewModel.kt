package com.gkkendor.app.ui.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkkendor.app.model.Report
import com.gkkendor.app.model.ReportResponse
import com.gkkendor.app.util.Constants
import com.gkkendor.app.util.Resource
import com.gkkendor.app.util.formatTo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class ReportViewModel : ViewModel() {
    companion object{
        const val TAG = "ReportViewModel"
    }
    val resources: MutableLiveData<Resource<ReportResponse>> = MutableLiveData()

    init {
        getReports()
    }

    fun getReports() = viewModelScope.launch {
        resources.postValue(Resource.Loading())
        try {
            val sdf = SimpleDateFormat(Constants.PATTERN_DATE)
            val reports = mutableListOf<Report>().apply {
                repeat(25){
                    val id = it + 1
                    add(Report(
                        id,
                        "Tittle $id",
                        Constants.randomString(255),
                        LocalDateTime.now().toString().formatTo(sdf),
                        Constants.statuses.random()
                    ))
                }
            }

            resources.postValue(Resource.Success(ReportResponse(
                reports,
                status = "Success",
                totalResults = reports.size
            )))
        }catch (t: Throwable){
            resources.postValue(Resource.Error(t.message ?: "Unknown"))
        }
    }
}
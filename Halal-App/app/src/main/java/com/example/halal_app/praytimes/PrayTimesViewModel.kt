package com.example.halal_app.praytimes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.halal_app.praytimes.network.PrayTimesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PrayTimesViewModel: ViewModel() {
    private val _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    private val _fajr = MutableLiveData<String>()
    val fajr: LiveData<String>
        get() = _fajr

    private val _sunrise = MutableLiveData<String>()
    val sunrise: LiveData<String>
        get() = _sunrise

    private val _dzuhur = MutableLiveData<String>()
    val dzuhur: LiveData<String>
        get() = _dzuhur

    private val _ashr = MutableLiveData<String>()
    val ashr: LiveData<String>
        get() = _ashr

    private val _maghrib = MutableLiveData<String>()
    val maghrib: LiveData<String>
        get() = _maghrib

    private val _isya = MutableLiveData<String>()
    val isya: LiveData<String>
        get() = _isya

    private val _isLoaded = MutableLiveData<Boolean>()
    val isLoaded: LiveData<Boolean>
        get() = _isLoaded

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main)

    init {
        _isLoaded.value = false
        getPrayTimesInfo()
    }

    private fun getPrayTimesInfo(){
        coroutineScope.launch {
            val getPrayTimes = PrayTimesApi.retrofitService.getProductAsync()
            try {
                val prayTimes = getPrayTimes.await()
                val currentDate = LocalDateTime.now() //https://www.programiz.com/kotlin-programming/examples/current-date-time
                val formatDate = DateTimeFormatter.ofPattern("yyyy") //https://www.programiz.com/kotlin-programming/examples/current-date-time
                val formattedDate = currentDate.format(formatDate) //https://www.programiz.com/kotlin-programming/examples/current-date-time
                _date.value = prayTimes.date + ".$formattedDate"
                _fajr.value = prayTimes.subuh
                _sunrise.value = prayTimes.terbit
                _dzuhur.value = prayTimes.dzuhur
                _ashr.value = prayTimes.ashr
                _maghrib.value = prayTimes.maghrib
                _isya.value = prayTimes.isya
                _isLoaded.value = true
            }catch (e: Exception){
                _date.value = "Not found: " + e.message
                _fajr.value = "none"
                _sunrise.value = "none"
                _dzuhur.value = "none"
                _ashr.value = "none"
                _maghrib.value = "none"
                _isya.value = "none"
                _isLoaded.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
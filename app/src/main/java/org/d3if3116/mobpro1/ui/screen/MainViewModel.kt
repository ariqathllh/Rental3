package org.d3if3116.mobpro1.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.d3if3116.mobpro1.model.Hewan
import org.d3if3116.mobpro1.network.HewanApi
import org.jetbrains.annotations.ApiStatus

class MainViewModel:ViewModel()  {
    var data = mutableStateOf(emptyList<Hewan>())
        private set
    var status = MutableStateFlow(org.d3if3116.mobpro1.network.ApiStatus.LOADING)
        private set
    init {
        retrieveData()
    }
    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = org.d3if3116.mobpro1.network.ApiStatus.LOADING
            try {
                data.value = HewanApi.service.getHewan()
                status.value = org.d3if3116.mobpro1.network.ApiStatus.SUCCES
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = org.d3if3116.mobpro1.network.ApiStatus.FAILED
            }
        }
    }
}
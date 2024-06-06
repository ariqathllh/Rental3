package org.d3if3116.mobpro1.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3116.mobpro1.model.Hewan
import org.d3if3116.mobpro1.network.HewanApi
import org.jetbrains.annotations.ApiStatus
import java.io.ByteArrayOutputStream

class MainViewModel:ViewModel()  {
    var data = mutableStateOf(emptyList<Hewan>())
        private set
    var status = MutableStateFlow(org.d3if3116.mobpro1.network.ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = org.d3if3116.mobpro1.network.ApiStatus.LOADING
            try {
                data.value = HewanApi.service.getHewan(userId)
                status.value = org.d3if3116.mobpro1.network.ApiStatus.SUCCES
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = org.d3if3116.mobpro1.network.ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, nama: String, namaLatin: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = HewanApi.service.postHewan(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    namaLatin.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null }
}
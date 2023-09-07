package com.ferhatt.cryptoappwithkoil.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ferhatt.cryptoappwithkoil.model.CryptoModel
import com.ferhatt.cryptoappwithkoil.repository.CryptoDownload
import com.ferhatt.cryptoappwithkoil.service.CryptoAPI
import com.ferhatt.cryptoappwithkoil.util.Resource
import com.ferhatt.cryptoappwithkoil.view.RecyclerViewAdapter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoViewModel(
    private val cryptoDownloadRepo : CryptoDownload
) : ViewModel() {

    val cryptoList = MutableLiveData<Resource<List<CryptoModel>>>()
    val cryptoError = MutableLiveData<Resource<Boolean>>()
    val cryptoLoading = MutableLiveData<Resource<Boolean>>()
    private var job : Job?= null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error : ${throwable.localizedMessage}")
        cryptoError.value = Resource.error(throwable.localizedMessage ?: "Error",data = true)
    }
    fun getDataFromAPI(){

        cryptoLoading.value = Resource.loading(data = true)
/*
        val BASE_URL = "https://raw.githubusercontent.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)

 */

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
          val resource = cryptoDownloadRepo.downloadCryptos()

            withContext(Dispatchers.Main){
                resource.data?.let {
                    cryptoList.value = resource
                    cryptoLoading.value = Resource.loading(data = false)
                    cryptoError.value = Resource.error("",data = false)
                }
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
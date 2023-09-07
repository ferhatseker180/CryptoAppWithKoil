package com.ferhatt.cryptoappwithkoil.di

import com.ferhatt.cryptoappwithkoil.repository.CryptoDownload
import com.ferhatt.cryptoappwithkoil.repository.CryptoDownloadImplement
import com.ferhatt.cryptoappwithkoil.service.CryptoAPI
import com.ferhatt.cryptoappwithkoil.viewmodel.CryptoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {

        val BASE_URL = "https://raw.githubusercontent.com/"

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)

    }

    single<CryptoDownload> {
        CryptoDownloadImplement(get())
    }

    viewModel {
        CryptoViewModel(get())
    }

}
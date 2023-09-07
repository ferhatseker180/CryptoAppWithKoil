package com.ferhatt.cryptoappwithkoil.repository

import com.ferhatt.cryptoappwithkoil.model.CryptoModel
import com.ferhatt.cryptoappwithkoil.service.CryptoAPI
import com.ferhatt.cryptoappwithkoil.util.Resource

class CryptoDownloadImplement(private val api : CryptoAPI) : CryptoDownload {
    override suspend fun downloadCryptos(): Resource<List<CryptoModel>>  {

        return try {
            val response = api.getData()
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error",null)
            } else {
                Resource.error("Error",null)
            }
        } catch (e : Exception){
            Resource.error("No data",null)
        }

    }
}
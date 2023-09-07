package com.ferhatt.cryptoappwithkoil.repository

import com.ferhatt.cryptoappwithkoil.model.CryptoModel
import com.ferhatt.cryptoappwithkoil.util.Resource

interface CryptoDownload {

    suspend fun downloadCryptos() : Resource<List<CryptoModel>>
}
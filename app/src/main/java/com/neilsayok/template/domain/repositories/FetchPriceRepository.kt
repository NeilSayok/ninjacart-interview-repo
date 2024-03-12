package com.neilsayok.template.domain.repositories

import com.neilsayok.template.data.model.FetchPriceResponse
import com.neilsayok.template.domain.services.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FetchPriceRepository @Inject constructor(private val apiInterface: ApiInterface) {
    suspend fun fetchPriceResponse(): FetchPriceResponse {
        return apiInterface.fetchPriceResponse()
    }
}
package com.neilsayok.template.domain.services

import com.neilsayok.template.data.constants.ApiConstants.API_URL_FETCH_PRICE
import com.neilsayok.template.data.model.FetchPriceResponse
import retrofit2.http.GET


interface ApiInterface {

    @GET(API_URL_FETCH_PRICE)
    suspend fun fetchPriceResponse(): FetchPriceResponse

}
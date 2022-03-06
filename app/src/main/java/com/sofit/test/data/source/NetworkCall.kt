package com.sofit.test.data.source

import com.sofit.test.data.model.ResponseCountryX
import com.sofit.test.data.model.ResponsePlayers
import com.sofit.test.data.model.ResponseTeam
import com.sofit.test.utils.AppConstant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

/**
 * Created by JAI on 11,November,2019
 * JAI KHAMBHAYTA
 */
@Singleton
interface NetworkCall {

    @GET(AppConstant.API_ALL)
    suspend fun getAllData(): Response<ResponseCountryX>

}
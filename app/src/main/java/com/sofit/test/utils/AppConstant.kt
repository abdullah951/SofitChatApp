package com.sofit.test.utils

/**
 * Created by JAI on 11,November,2019
 * JAI KHAMBHAYTA
 */
class AppConstant {
    companion object {

        val DB_NAME = "blueprint.db"
        val PREFERENCE_NAME = "blueprint_pref"
        val TABLE_NAME = "jsonresponse"
        val NULL_INDEX = -1L

        const val DEBUG_TAG: String = ("Jai")
        const val data = "https://reqres.in/api/users?page=1"

        /// API FOR CRICKET DATA
        const val BASE_URL: String = ("https://cricket.sportmonks.com/api/v2.0/")
        const val BASE_URL2: String = ("https://restcountries.com/v2/")
        const val API_ALL: String = "all"
        const val API_TEAMS: String = "teams"
        const val API_PLAYERS: String = "players"
        const val API_TOKEN_VALUE: String =
            "azqYNh2trcon5GsnYHKhNQZvbmhwCQl7rNJlx6BkOyN8KBJZ4Bf4esB6mQYK"

        //Shared Preferences reference
        const val API_TOKEN_KEY: String = "api_token"
        const val PREF_NAME = "NAME"
        const val PREF_COUNTRY = "COUNTRY"
        const val PREF_PIC_URI = "PIC_URI"
        const val PREF_EMAIL = "EMAIL"

    }
}
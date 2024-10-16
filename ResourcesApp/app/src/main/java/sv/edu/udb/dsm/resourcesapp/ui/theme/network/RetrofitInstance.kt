package sv.edu.udb.dsm.resourcesapp.ui.theme.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sv.edu.udb.dsm.resourcesapp.ui.theme.api.ApiService

class RetrofitInstance {
    companion object {
        private const val BASE_URL = "https://663bb7a5fee6744a6ea2b410.mockapi.io/"  // Tu URL base


        val api: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}

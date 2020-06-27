package dev.vedantm.samachar.api

import androidx.annotation.RestrictTo
import dev.vedantm.samachar.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFitInstance {
    companion object{
        private val retrofit by lazy {
            val logging=HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            //Logging ka portion jo hai vo basically humara morgan jaisa hai

            val client=OkHttpClient.Builder().addInterceptor(logging)
                .build()

            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())  //convertor factory shayad vaisa hai jaisa app.use(express.json())
                .client(client).build()

        }

        val api by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }
}
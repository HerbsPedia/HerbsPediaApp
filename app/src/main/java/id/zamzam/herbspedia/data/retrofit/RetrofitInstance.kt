import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance  {
    private const val BASE_URL = "https://api-endpoint-dgonny5rzq-et.a.run.app/api/"  // Gunakan 10.0.2.2 untuk emulator, ganti dengan IP yang sesuai untuk perangkat fisik

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        instance.create(ApiService::class.java)
    }

}
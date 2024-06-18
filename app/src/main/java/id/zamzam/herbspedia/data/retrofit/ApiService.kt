
import id.zamzam.herbspedia.data.pref.Plant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("plants")
fun getPlants(): Call<List<Plant>>
//    suspend fun getPlants(): Response<List<Plant>>

    @GET("plants/Kategory/{category}")
    suspend fun getPlantsByCategory(@Path("category") category: String): Response<List<Plant>>
}
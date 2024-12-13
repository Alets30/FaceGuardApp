import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header

data class ReconocimientoRequest(val usuario: String, val photo: String)
data class ReconocimientoResponse(val success: String, val result: String)

interface ReconocimientoApiService {
    @FormUrlEncoded
    @POST("login/")
    fun reconocimiento(
        @Field("usuario") username: String,
        @Field("photo") image: String
    ): Call<ReconocimientoResponse>
}
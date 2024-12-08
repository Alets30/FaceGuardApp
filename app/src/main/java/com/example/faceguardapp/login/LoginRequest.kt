import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Header

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(
    val token: String,
    val username: String
)

//data class ProfileRequest(val token: String)
data class ProfileResponse(val username: String)

interface AuthApiService {
    @POST("api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/profile")
    fun profile(): Call<ProfileResponse>
}
package br.com.disapps.homepet.data.ws

import br.com.disapps.homepet.data.model.Auth
import br.com.disapps.homepet.data.model.Coordinate
import br.com.disapps.homepet.data.model.User
import br.com.disapps.homepet.data.ws.request.HotelsRequest
import br.com.disapps.homepet.data.ws.request.PasswordLoginRequest
import br.com.disapps.homepet.data.ws.request.RefreshTokenLoginRequest
import br.com.disapps.homepet.data.ws.request.SignupRequest
import br.com.disapps.homepet.data.ws.response.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by diefferson.santos on 23/08/17.
 */

interface RestApi {

    @POST("auth")
    fun authLogin(@Header("Authorization") clientSecret: String, @Body request: PasswordLoginRequest): Observable<AuthResponse>

    @POST("auth")
    fun refreshToken(@Header("Authorization") clientSecret: String, @Body request: RefreshTokenLoginRequest): Call<AuthResponse>

    @POST("signup")
    fun signup(@Body request: SignupRequest): Observable<SignupResponse>

    @GET("user")
    fun getUser(@Header("Authorization") accessToken: String) : Observable<UserResponse>

    @GET("hotel")
    fun getHotels(@Query("sort") sort: String?, @Query("sense") sense: String?): Observable<ListHotelResponse>

    @GET("hotel/{codeHotel}")
    fun getHotel(@Path("codeHotel") codeHotel: Int): Observable<HotelResponse>

    @GET("comment/{codeHotel}")
    fun getComments(@Path("codeHotel") codeHotel: Int): Observable<ListCommentResponse>

    @GET("coordinates/{codeHotel}")
    fun getCoordinates(@Path("codeHotel") codeHotel: Int) : Observable<CoordinateResponse>
}

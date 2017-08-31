package br.com.disapps.homepet.data.cache

import android.content.Context
import br.com.disapps.homepet.data.model.Hotel
import br.com.disapps.homepet.data.prefs.Preferences
import br.com.disapps.homepet.data.ws.RestApi
import br.com.disapps.homepet.data.ws.response.ApiListResponse
import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictProvider
import java.io.File

/**
 * Created by diefferson.santos on 31/08/17.
 */
class HotelRepository (private val context: Context, preferences: Preferences, cacheDir: File, restApi: RestApi)
    : BaseRepository(preferences, cacheDir, restApi) {

    fun getHoteis(hasInternetConnection: Boolean): Observable<ApiListResponse<Hotel>> {
        return providers.getHoteis(
                restApi.getHoteis(),
                DynamicKey(this.preferences.auth.refreshToken),
                EvictProvider(hasInternetConnection))
    }
}
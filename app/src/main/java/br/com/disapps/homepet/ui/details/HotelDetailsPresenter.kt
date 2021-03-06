package br.com.disapps.homepet.ui.details

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

import br.com.disapps.homepet.data.cache.HotelRepository
import br.com.disapps.homepet.data.prefs.Preferences
import br.com.disapps.homepet.data.ws.RestApi
import br.com.disapps.homepet.data.ws.request.IncludeRatingRequest
import br.com.disapps.homepet.data.ws.response.ApiSimpleResponse
import br.com.disapps.homepet.data.ws.response.IncludeResponse
import br.com.disapps.homepet.util.rx.IErrorHandlerHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by diefferson.santos on 31/08/17.
 */

class HotelDetailsPresenter(private val mHotelRepository: HotelRepository,
                            val restApi: RestApi,
                            val preferences: Preferences) : MvpBasePresenter<IHotelDetailsView>() {

    private val disposables = CompositeDisposable()

    fun loadHotel(codeHotel: Int) {

        if (isViewAttached) {
            view.showLoading(false)
        }

        disposables.add(mHotelRepository.getHotel(true, codeHotel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ApiSimpleResponse.HotelResponse>() {
                    override fun onNext(response: ApiSimpleResponse.HotelResponse) {

                        val hotel = response.content

                        if (isViewAttached) {
                            view.fillHotelDetails(hotel!!)
                            view.dismissLoading()
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            IErrorHandlerHelper.defaultErrorResolver(this@HotelDetailsPresenter.view, e)
                            view.dismissLoading()
                        }
                    }

                    override fun onComplete() {

                    }
                })
        )
    }

    fun sendRating(rating: Float, codeHotel: Int){

        var request = IncludeRatingRequest(codeHotel, rating)

        if(isViewAttached){
            view.showLoading(true)
        }

        disposables.add(restApi.postRating(preferences.authTokenWithPrefix, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<IncludeResponse>() {
                    override fun onNext(response: IncludeResponse) {

                        if(response.message.equals("Sucess")){
                            if (isViewAttached) {
                                view.fillSuccesInclude()
                                view.dismissLoading()
                            }
                        }else{
                            view.fillErrorInclude(response.message)
                            view.dismissLoading()
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            IErrorHandlerHelper.defaultErrorResolver(this@HotelDetailsPresenter.view, e)
                            view.dismissLoading()
                        }
                    }

                    override fun onComplete() {

                    }
                }))


    }

    override fun detachView(retainInstance: Boolean) {
        disposables.clear()
    }

}

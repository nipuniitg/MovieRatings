package com.fenchtose.movieratings.features.search_page

import com.fenchtose.movieratings.base.Presenter
import com.fenchtose.movieratings.model.Movie
import com.fenchtose.movieratings.model.api.provider.MovieProvider
import com.fenchtose.movieratings.model.db.like.LikeStore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SearchPresenter(private val provider: MovieProvider, private val likeStore: LikeStore) : Presenter<SearchPage>() {

    private var currentQuery = ""
    private var data: ArrayList<Movie>? = null

    init {
        provider.addPreferenceApplier(likeStore)
    }

    override fun attachView(view: SearchPage) {
        super.attachView(view)
        data?.let {
            getView()?.setData(it)
        }
    }

    fun onSearchRequested(query: String) {

        if (currentQuery == query && data != null) {
            getView()?.setData(data!!)
            return
        }

        getView()?.showLoading(true)

        currentQuery = query

        val d = provider.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (it.success) {
                                getView()?.setData(it.results)
                                data = it.results
                            } else {
                                data = null
                            }
                        },
                        onError = {
                            it.printStackTrace()
                            data = null
                        }
                )

        subscribe(d)
    }

    fun onQueryCleared() {
        currentQuery = ""
        data = null
    }

    fun setLiked(movie: Movie) {
        likeStore.setLiked(movie.imdbId, !movie.liked)
    }
}
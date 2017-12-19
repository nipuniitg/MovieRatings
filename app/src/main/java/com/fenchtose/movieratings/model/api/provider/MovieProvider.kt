package com.fenchtose.movieratings.model.api.provider

import com.fenchtose.movieratings.model.Movie
import com.fenchtose.movieratings.model.SearchResult
import com.fenchtose.movieratings.model.db.UserPreferneceApplier
import io.reactivex.Observable

interface MovieProvider {
    fun getMovie(title: String): Observable<Movie>
    fun search(title: String): Observable<SearchResult>
    fun addPreferenceApplier(applier: UserPreferneceApplier)
}
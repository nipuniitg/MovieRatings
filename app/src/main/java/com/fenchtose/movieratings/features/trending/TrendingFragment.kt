package com.fenchtose.movieratings.features.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.fenchtose.movieratings.MovieRatingsApplication
import com.fenchtose.movieratings.R
import com.fenchtose.movieratings.base.RouterPath
import com.fenchtose.movieratings.features.baselistpage.BaseMovieListPageFragment
import com.fenchtose.movieratings.model.db.like.DbLikeStore
import com.fenchtose.movieratings.util.AppRxHooks
import com.fenchtose.movieratings.widgets.IndicatorTabLayout

class TrendingFragment: BaseMovieListPageFragment<TrendingFragment, TrendingPresenter>() {

//    private var tabLayout: TabLayout? = null

    override fun createPresenter(): TrendingPresenter {
        val likeStore = DbLikeStore.getInstance(MovieRatingsApplication.database.favDao())

        return TrendingPresenter(
                AppRxHooks(),
                MovieRatingsApplication.ratingProviderModule.ratingProvider,
                MovieRatingsApplication.database.movieDao(),
                likeStore,
                setOf(likeStore),
                path?.getRouter()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout: IndicatorTabLayout? = view.findViewById(R.id.tabs)
        tabLayout?.let {
            it.addTab(createTab("Today"))
            it.addTab(createTab("This week"))

            it.selectTab(
                    when(presenter?.currentPeriod()) {
                        "day" -> 0
                        "week" -> 1
                        else -> 0
                    }
            )

            it.addListener {
                presenter?.updatePeriod(
                        when(it) {
                            0 -> "day"
                            1 -> "week"
                            else -> "day"
                        }
                )
            }
        }
    }

    private fun createTab(text: String): IndicatorTabLayout.Tab {
        val view = LayoutInflater.from(context).inflate(R.layout.tab_item_layout, null).apply { (this as TextView).text = text }
        return IndicatorTabLayout.Tab(view)
    }

    override fun getErrorContent() = R.string.trending_page_error_content
    override fun getEmptyContent() = R.string.trending_page_empty_content
    override fun canGoBack() = true
    override fun getScreenTitle() = R.string.trending_screen_title

    override fun getLayout(): Int {
        return R.layout.trending_movies_page_layout
    }

}

class TrendingPath: RouterPath<TrendingFragment>() {
    override fun createFragmentInstance(): TrendingFragment {
        return TrendingFragment()
    }
}
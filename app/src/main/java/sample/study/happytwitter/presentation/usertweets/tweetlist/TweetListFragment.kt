package sample.study.happytwitter.presentation.usertweets.tweetlist

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.usertweets_tweetlist_view.profile_background_imageview
import kotlinx.android.synthetic.main.usertweets_tweetlist_view.profile_picture_imageview
import kotlinx.android.synthetic.main.usertweets_tweetlist_view.secured_content_group
import kotlinx.android.synthetic.main.usertweets_tweetlist_view.tweets_loading_progressbar
import kotlinx.android.synthetic.main.usertweets_tweetlist_view.tweets_recyclerview
import sample.study.happytwitter.R
import sample.study.happytwitter.base.view.BaseFragment
import sample.study.happytwitter.base.view.IView
import sample.study.happytwitter.data.objects.TwitterUser
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.LoadTweetsRequestState
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListIntent
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListIntent.AnalyseTweetIntent
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListIntent.InitIntent
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListViewState
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetRecyclerAdapter
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class TweetListFragment : BaseFragment(), IView<TweetListIntent, TweetListViewState> {

  companion object {
    const val EXTRA_USER_TWITTER = "EXTRA_USER_TWITTER"

    fun newInstance(twitterUser: TwitterUser): TweetListFragment {
      val args = Bundle()
      args.putParcelable(EXTRA_USER_TWITTER, twitterUser)
      val fragment = TweetListFragment()
      fragment.arguments = args
      return fragment
    }
  }

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: TweetListViewModel by lazy(NONE) {
    ViewModelProviders.of(this, viewModelFactory)[TweetListViewModel::class.java]
  }

  private val disposables = CompositeDisposable()

  private lateinit var twitterUser: TwitterUser

  private val tweetListAdapter = TweetRecyclerAdapter()

  override fun onCreateView(inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.usertweets_tweetlist_view, container, false)
  }

  override fun onViewCreated(view: View,
      savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    twitterUser = arguments!!.getParcelable(EXTRA_USER_TWITTER)

    Picasso.get()
        .load(twitterUser.profile_banner_url)
        .into(profile_background_imageview)

    Picasso.get()
        .load(twitterUser.profile_image_url)
        .placeholder(R.drawable.profile_img_placeholder)
        .error(R.drawable.profile_img_placeholder)
        .into(profile_picture_imageview)

    profile_picture_imageview.borderColor = Color.parseColor("#${twitterUser.profile_link_color}")

    //collapsing_toolbar.title = "${twitterUser.name} @${twitterUser.screen_name}"
    tweets_recyclerview.adapter = tweetListAdapter

    // Subscribe to the viewModel and call render for every emitted state
    disposables.add(viewModel.states().subscribe(this::render))
    // Pass the UI's intents to the ViewModel
    viewModel.intentsHandler(intents())
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.clear()
  }

  private fun initialIntent(): Observable<InitIntent> {
    return Observable.just(TweetListIntent.InitIntent(twitterUser))
  }

  private fun analyseTweetIntent(): Observable<AnalyseTweetIntent> {
    return tweetListAdapter.itemClickObservable.map { TweetListIntent.AnalyseTweetIntent(it) }
  }

  override fun intents(): Observable<TweetListIntent> {
    return Observable.merge(initialIntent(), analyseTweetIntent())
  }

  private var loadTweetsError: Throwable? = null

  override fun render(state: TweetListViewState) {
    Log.d("ViewModelRender", "render called. ${state.loadedTweetList.size}")
    var isLoading = false
    var isPrivate = false
    var error: Throwable? = null
    state.loadTweetsRequestState?.let {
      when (it) {
        is LoadTweetsRequestState.Loading -> isLoading = true
        is LoadTweetsRequestState.ErrorPrivateTweets -> isPrivate = true
        is LoadTweetsRequestState.Error -> error = it.error
      }
    }

    tweets_loading_progressbar.visibility = if (isLoading) {
      View.VISIBLE
    } else {
      View.GONE
    }

    secured_content_group.visibility = if (isPrivate) {
      View.VISIBLE
    } else {
      View.GONE
    }

    tweetListAdapter.refreshList(state.loadedTweetList)
    tweets_recyclerview.visibility = if (state.loadedTweetList.isNotEmpty()) {
      View.VISIBLE
    } else {
      View.GONE
    }

    if (loadTweetsError != error) {
      loadTweetsError = error
      showToast(loadTweetsError)
    }
  }
}
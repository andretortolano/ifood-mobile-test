package sample.study.happytwitter.presentation.usertweets.tweetlist

import sample.study.happytwitter.base.mvi.IViewState
import sample.study.happytwitter.base.network.NetworkingViewState
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetItemState

data class TweetListViewState(
    val loadTweetsNetworking: NetworkingViewState = NetworkingViewState.Idle,
    val loadedTweetList: ArrayList<TweetItemState>? = null
) : IViewState {

  companion object {
    const val ERROR_PRIVATE_USER = "ERROR_PRIVATE_USER"
  }
}
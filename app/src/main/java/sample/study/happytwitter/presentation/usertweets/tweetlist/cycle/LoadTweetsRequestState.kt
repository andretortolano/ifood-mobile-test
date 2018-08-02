package sample.study.happytwitter.presentation.usertweets.tweetlist.cycle

import sample.study.happytwitter.base.IViewState

sealed class LoadTweetsRequestState : IViewState {
  object Loading : LoadTweetsRequestState()
  object ErrorPrivateTweets : LoadTweetsRequestState()
  object Success : LoadTweetsRequestState()
  data class Error(val error: Throwable) : LoadTweetsRequestState()
}
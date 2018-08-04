package sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem

import sample.study.happytwitter.base.mvi.IViewState

sealed class AnalyzeTweetRequestState : IViewState {
  object Loading : AnalyzeTweetRequestState()
  object Success : AnalyzeTweetRequestState()
  data class Error(val error: Throwable) : AnalyzeTweetRequestState()
}
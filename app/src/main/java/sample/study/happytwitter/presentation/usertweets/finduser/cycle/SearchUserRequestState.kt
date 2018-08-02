package sample.study.happytwitter.presentation.usertweets.finduser.cycle

import sample.study.happytwitter.base.IViewState
import sample.study.happytwitter.data.objects.TwitterUser

sealed class SearchUserRequestState : IViewState {
  object Searching : SearchUserRequestState()
  object UserNotFound : SearchUserRequestState()
  object UserDisabled : SearchUserRequestState()
  data class Success(val twitterUser: TwitterUser) : SearchUserRequestState()
  data class Error(val error: Throwable) : SearchUserRequestState()
}
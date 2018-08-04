package sample.study.happytwitter.presentation.usertweets.finduser

import sample.study.happytwitter.base.mvi.IResult
import sample.study.happytwitter.data.objects.TwitterUser

sealed class FindUserResult : IResult {
  sealed class ChangeUserResult : FindUserResult() {
    object Valid : ChangeUserResult()
    object Invalid : ChangeUserResult()
  }

  sealed class SearchUserResult : FindUserResult() {
    object Loading : SearchUserResult()
    data class Success(val user: TwitterUser) : SearchUserResult()
    object UserNotFound : SearchUserResult()
    object UserDisabled : SearchUserResult()
    data class UnknownError(val error: Throwable) : SearchUserResult()
  }
}
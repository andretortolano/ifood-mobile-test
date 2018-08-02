package sample.study.happytwitter.presentation.usertweets.finduser.cycle

import sample.study.happytwitter.base.IResult
import sample.study.happytwitter.data.objects.TwitterUser

sealed class FindUserResult : IResult {
  sealed class ChangeUserResult : FindUserResult() {
    class Valid : ChangeUserResult()
    class Invalid : ChangeUserResult()
  }

  sealed class SearchUserResult : FindUserResult() {
    class Loading : SearchUserResult()
    data class Success(val user: TwitterUser) : SearchUserResult()
    data class UnknownError(val error: Throwable) : SearchUserResult()
    class UserNotFound : SearchUserResult()
    class UserDisabled : SearchUserResult()
  }
}
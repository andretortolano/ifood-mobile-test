package sample.study.happytwitter.presentation.usertweets.finduser.cycle

import sample.study.happytwitter.base.IIntent

sealed class FindUserIntent : IIntent {
  data class ChangeUserIntent(val username: String) : FindUserIntent()
  data class SearchButtonIntent(val username: String) : FindUserIntent()
  data class SearchSoftKeyboardIntent(val username: String): FindUserIntent()
}
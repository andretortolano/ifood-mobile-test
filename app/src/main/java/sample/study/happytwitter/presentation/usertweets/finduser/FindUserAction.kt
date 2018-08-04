package sample.study.happytwitter.presentation.usertweets.finduser

import sample.study.happytwitter.base.mvi.IAction

sealed class FindUserAction : IAction {
  data class ChangeUserAction(val username: String) : FindUserAction()
  data class SearchUserAction(val username: String) : FindUserAction()
}
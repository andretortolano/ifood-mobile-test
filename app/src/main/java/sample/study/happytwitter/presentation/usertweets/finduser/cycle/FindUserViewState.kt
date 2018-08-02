package sample.study.happytwitter.presentation.usertweets.finduser.cycle

import sample.study.happytwitter.base.IViewState

data class FindUserViewState(
    val searchEnabled: Boolean = false,
    val searchUserRequestState: SearchUserRequestState? = null
) : IViewState
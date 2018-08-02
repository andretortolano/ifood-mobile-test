package sample.study.happytwitter.presentation.usertweets.finduser

import io.reactivex.functions.BiFunction
import sample.study.happytwitter.base.viewmodel.reducer.IStateReducer
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserResult
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserViewState
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.SearchUserRequestState
import javax.inject.Inject

class FindUserStateReducer @Inject constructor() :
    IStateReducer<FindUserResult, FindUserViewState> {

  override fun reduce(): BiFunction<FindUserViewState, FindUserResult, FindUserViewState> {
    return BiFunction { oldState: FindUserViewState, result: FindUserResult ->
      when (result) {
        is FindUserResult.ChangeUserResult.Valid -> oldState.copy(searchEnabled = true)
        is FindUserResult.ChangeUserResult.Invalid -> oldState.copy(searchEnabled = false)
        is FindUserResult.SearchUserResult.Loading -> oldState.copy(searchUserRequestState = SearchUserRequestState.Searching)
        is FindUserResult.SearchUserResult.Success -> oldState.copy(searchUserRequestState = SearchUserRequestState.Success(result.user))
        is FindUserResult.SearchUserResult.UserNotFound -> oldState.copy(searchUserRequestState = SearchUserRequestState.UserNotFound)
        is FindUserResult.SearchUserResult.UserDisabled -> oldState.copy(searchUserRequestState = SearchUserRequestState.UserDisabled)
        is FindUserResult.SearchUserResult.UnknownError -> oldState.copy(searchUserRequestState = SearchUserRequestState.Error(result.error))
      }
    }
  }
}
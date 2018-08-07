package sample.study.happytwitter.presentation.usertweets.finduser

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import retrofit2.HttpException
import sample.study.happytwitter.base.mvi.BaseViewModel
import sample.study.happytwitter.base.network.NetworkingViewState
import sample.study.happytwitter.data.twitter.ITwitterRepo
import sample.study.happytwitter.data.twitter.TwitterRepository
import sample.study.happytwitter.data.twitter.remote.TwitterError
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserAction.ChangeUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserAction.SearchUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserResult.ChangeUserResult
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserResult.SearchUserResult
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserResult.SearchUserResult.UnknownError
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserResult.SearchUserResult.UserDisabled
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserResult.SearchUserResult.UserNotFound
import sample.study.happytwitter.utils.schedulers.ISchedulerProvider
import javax.inject.Inject

class FindUserViewModel @Inject constructor(
    private val twitterRepository: ITwitterRepo,
    private val schedulerProvider: ISchedulerProvider
) : BaseViewModel<FindUserAction, FindUserResult, FindUserViewState>() {

  override val startAction: Class<out FindUserAction>?
    get() = null

  override val initialViewState: FindUserViewState
    get() = FindUserViewState()

  private val changeUserProcessor = ObservableTransformer<ChangeUserAction, ChangeUserResult> { actions ->
    actions.map { action ->
      if (action.username.isNotEmpty()) {
        ChangeUserResult.Valid
      } else {
        ChangeUserResult.Invalid
      }
    }
  }

  private val searchUserProcessor = ObservableTransformer<SearchUserAction, SearchUserResult> { actions ->
    actions.flatMap { action ->
      twitterRepository.getUser(action.username)
          // replace _normal from image so that we have a decent profile image to load.
          .map { user -> user.copy(profile_image_url = user.profile_image_url?.replace("_normal", "_400x400")) }
          .map(SearchUserResult::Success)
          .cast(SearchUserResult::class.java)
          .onErrorReturn { error ->
            if (error is HttpException) {
              when (TwitterError(error).code) {
                50 -> UserNotFound
                63 -> UserDisabled
                else -> UnknownError(error)
              }
            } else {
              UnknownError(error)
            }
          }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(SearchUserResult.Loading)
    }
  }

  override fun actionProcessor(): ObservableTransformer<FindUserAction, FindUserResult> {
    return ObservableTransformer { actions ->
      actions.publish { shared ->
        Observable.merge(
          shared.ofType(ChangeUserAction::class.java).compose(changeUserProcessor),
          shared.ofType(SearchUserAction::class.java).compose(searchUserProcessor)
        )
      }
    }
  }

  override fun stateReducer(): BiFunction<FindUserViewState, FindUserResult, FindUserViewState> {
    return BiFunction { oldState: FindUserViewState, result: FindUserResult ->
      when (result) {
        is FindUserResult.ChangeUserResult.Valid -> oldState.copy(isUserNameValid = true)
        is FindUserResult.ChangeUserResult.Invalid -> oldState.copy(isUserNameValid = false)
        is FindUserResult.SearchUserResult.Loading -> oldState.copy(searchUserNetworking = NetworkingViewState.Loading)
        is FindUserResult.SearchUserResult.Success -> oldState.copy(searchUserNetworking = NetworkingViewState.Success, twitterUser = result.user)
        is FindUserResult.SearchUserResult.UserNotFound -> oldState.copy(searchUserNetworking = NetworkingViewState.Error(FindUserViewState.ERROR_USER_NOT_FOUND))
        is FindUserResult.SearchUserResult.UserDisabled -> oldState.copy(searchUserNetworking = NetworkingViewState.Error(FindUserViewState.ERROR_USER_DISABLED))
        is FindUserResult.SearchUserResult.UnknownError -> oldState.copy(searchUserNetworking = NetworkingViewState.Error(result.error.message))
      }
    }
  }
}
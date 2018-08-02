package sample.study.happytwitter.presentation.usertweets.finduser

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import retrofit2.HttpException
import sample.study.happytwitter.base.viewmodel.processor.IActionProcessor
import sample.study.happytwitter.data.objects.TwitterError
import sample.study.happytwitter.data.retrofit.TwitterAPI
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserAction.ChangeUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserAction.SearchUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserResult
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserResult.ChangeUserResult
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserResult.SearchUserResult
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserResult.SearchUserResult.UnknownError
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserResult.SearchUserResult.UserDisabled
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserResult.SearchUserResult.UserNotFound
import sample.study.happytwitter.utils.schedulers.ISchedulerProvider
import javax.inject.Inject

class FindUserActionProcessor @Inject constructor(
    private val twitterAPI: TwitterAPI,
    private val schedulerProvider: ISchedulerProvider
) : IActionProcessor<FindUserAction, FindUserResult> {

  override fun process(): ObservableTransformer<FindUserAction, FindUserResult> {
    return ObservableTransformer { actions ->
      actions.publish { shared ->
        Observable.merge(
          shared.ofType(ChangeUserAction::class.java).compose(changeUserProcessor),
          shared.ofType(SearchUserAction::class.java).compose(searchUserProcessor)
        )
      }
    }
  }

  private val changeUserProcessor = ObservableTransformer<ChangeUserAction, ChangeUserResult> { actions ->
    actions.map { action ->
      if (action.username.isNotEmpty()) {
        ChangeUserResult.Valid()
      } else {
        ChangeUserResult.Invalid()
      }
    }
  }

  private val searchUserProcessor = ObservableTransformer<SearchUserAction, SearchUserResult> { actions ->
    actions.flatMap { action ->
      twitterAPI.getUser(action.username)
          .toObservable()
          // replace _normal from image so that we have a decent profile image to load.
          .map { user -> user.copy(profile_image_url = user.profile_image_url?.replace("_normal", "_400x400")) }
          .map(SearchUserResult::Success)
          .cast(SearchUserResult::class.java)
          .onErrorReturn { error ->
            if (error is HttpException) {
              when (TwitterError(error).code) {
                50 -> UserNotFound()
                63 -> UserDisabled()
                else -> UnknownError(error)
              }
            } else {
              UnknownError(error)
            }
          }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(SearchUserResult.Loading())
    }
  }
}
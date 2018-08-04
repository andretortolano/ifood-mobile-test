package sample.study.happytwitter.presentation.usertweets.tweetlist

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import retrofit2.HttpException
import sample.study.happytwitter.base.mvi.BaseViewModel
import sample.study.happytwitter.base.network.NetworkingViewState
import sample.study.happytwitter.data.objects.GoogleAnalyzeBody
import sample.study.happytwitter.data.retrofit.GoogleAPI
import sample.study.happytwitter.data.retrofit.TwitterAPI
import sample.study.happytwitter.presentation.usertweets.tweetlist.TweetListAction.AnalyzeTweetAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.TweetListAction.LoadTweetsAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.TweetListResult.AnalyzeTweetResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.TweetListResult.LoadTweetsResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.TweetListViewState.Companion.ERROR_PRIVATE_USER
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.AnalyzeTweetRequestState
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetItemState
import sample.study.happytwitter.utils.GoogleUtils
import sample.study.happytwitter.utils.schedulers.ISchedulerProvider
import javax.inject.Inject

class TweetListViewModel @Inject constructor(
    private val twitterAPI: TwitterAPI,
    private val googleAPI: GoogleAPI,
    private val schedulerProvider: ISchedulerProvider
) : BaseViewModel<TweetListAction, TweetListResult, TweetListViewState>() {

  override val initialViewState: TweetListViewState
    get() = TweetListViewState()

  override val startAction: Class<out TweetListAction>?
    get() = LoadTweetsAction::class.java

  private val loadTweetsProcessor = ObservableTransformer<LoadTweetsAction, LoadTweetsResult> { actions ->
    actions.flatMap { action ->
      twitterAPI.getTweetsByUser(action.screenName)
          .toObservable()
          .map(LoadTweetsResult::Success)
          .cast(LoadTweetsResult::class.java)
          .onErrorReturn { error ->
            if (error is HttpException) {
              when (error.code()) {
                401 -> LoadTweetsResult.PrivateList
                else -> LoadTweetsResult.UnknownError(error)
              }
            } else {
              LoadTweetsResult.UnknownError(error)
            }
          }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(LoadTweetsResult.Loading)
    }
  }

  private val analyseTweetSentimentProcessor = ObservableTransformer<AnalyzeTweetAction, AnalyzeTweetResult> { actions ->
    actions.flatMap { action ->
      googleAPI.analyzeSentiment(GoogleAnalyzeBody.newInstance(action.listItem.tweet.text))
          .toObservable()
          .map { AnalyzeTweetResult.Success(action.listItem, GoogleUtils.mapSentiment(it)) }
          .cast(AnalyzeTweetResult::class.java)
          .onErrorReturn { AnalyzeTweetResult.UnknownError(action.listItem, it) }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(AnalyzeTweetResult.Loading(action.listItem))
    }
  }

  override fun actionProcessor(): ObservableTransformer<TweetListAction, TweetListResult> {
    return ObservableTransformer { actions ->
      actions.publish { shared ->
        Observable.merge(
          shared.ofType(LoadTweetsAction::class.java).compose(loadTweetsProcessor),
          shared.ofType(AnalyzeTweetAction::class.java).compose(analyseTweetSentimentProcessor)
        )
      }
    }
  }

  override fun stateReducer(): BiFunction<TweetListViewState, TweetListResult, TweetListViewState> {
    return BiFunction { oldState: TweetListViewState, result: TweetListResult ->
      when (result) {
        is LoadTweetsResult -> when (result) {
          is LoadTweetsResult.Loading -> oldState.copy(loadTweetsNetworking = NetworkingViewState.Loading)
          is LoadTweetsResult.Success -> oldState.copy(loadTweetsNetworking = NetworkingViewState.Success, loadedTweetList = ArrayList(result.tweets.map { TweetItemState(it) }))
          is LoadTweetsResult.PrivateList -> oldState.copy(loadTweetsNetworking = NetworkingViewState.Error(ERROR_PRIVATE_USER))
          is LoadTweetsResult.UnknownError -> oldState.copy(loadTweetsNetworking = NetworkingViewState.Error(result.error.message))
        }

        is AnalyzeTweetResult -> {
          val newList = arrayListOf<TweetItemState>()
          newList.addAll(oldState.loadedTweetList!!)
          val position = newList.indexOfFirst { it.tweet.id == result.tweetId }

          when (result) {
            is AnalyzeTweetResult.Loading -> {
              newList[position] = newList[position].copy(analyzeSentimentRequestState = AnalyzeTweetRequestState.Loading)
            }
            is AnalyzeTweetResult.Success -> {
              newList[position] =
                  newList[position].copy(analyzeSentimentRequestState = AnalyzeTweetRequestState.Success, sentiment = result.sentiment)
            }
            is AnalyzeTweetResult.UnknownError -> {
              newList[position] = newList[position].copy(analyzeSentimentRequestState = AnalyzeTweetRequestState.Error(result.error))
            }
          }
          oldState.copy(loadedTweetList = newList)
        }
      }
    }
  }
}
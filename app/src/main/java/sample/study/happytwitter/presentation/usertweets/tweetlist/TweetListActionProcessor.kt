package sample.study.happytwitter.presentation.usertweets.tweetlist

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import retrofit2.HttpException
import sample.study.happytwitter.base.viewmodel.processor.IActionProcessor
import sample.study.happytwitter.data.objects.GoogleAnalyzeBody
import sample.study.happytwitter.data.objects.GoogleAnalyzeResponse
import sample.study.happytwitter.data.retrofit.GoogleAPI
import sample.study.happytwitter.data.retrofit.TwitterAPI
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListAction.AnalyzeTweetAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListAction.LoadTweetsAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListResult.AnalyzeTweetResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListResult.LoadTweetsResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.HAPPY
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.NEUTRAL
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.SAD
import sample.study.happytwitter.utils.schedulers.ISchedulerProvider
import javax.inject.Inject

class TweetListActionProcessor @Inject constructor(
    private val twitterAPI: TwitterAPI,
    private val googleAPI: GoogleAPI,
    private val schedulerProvider: ISchedulerProvider
) : IActionProcessor<TweetListAction, TweetListResult> {

  override fun process(): ObservableTransformer<TweetListAction, TweetListResult> {
    return ObservableTransformer { actions ->
      actions.publish { shared ->
        Observable.merge(
          shared.ofType(LoadTweetsAction::class.java).compose(loadTweetsProcessor),
          shared.ofType(AnalyzeTweetAction::class.java).compose(analyseTweetSentimentProcessor)
        )
      }
    }
  }

  private val loadTweetsProcessor = ObservableTransformer<LoadTweetsAction, LoadTweetsResult> { actions ->
    actions.flatMap { action ->
      twitterAPI.getTweetsByUser(action.screenName)
          .toObservable()
          .map(LoadTweetsResult::Success)
          .cast(LoadTweetsResult::class.java)
          .onErrorReturn { error ->
            if (error is HttpException) {
              when (error.code()) {
                401 -> LoadTweetsResult.PrivateList()
                else -> LoadTweetsResult.UnknownError(error)
              }
            } else {
              LoadTweetsResult.UnknownError(error)
            }
          }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(LoadTweetsResult.Loading())
    }
  }

  private val analyseTweetSentimentProcessor = ObservableTransformer<AnalyzeTweetAction, AnalyzeTweetResult> { actions ->
    actions.flatMap { action ->
      googleAPI.analyzeSentiment(GoogleAnalyzeBody.newInstance(action.listItem.tweet.text))
          .toObservable()
          .map { AnalyzeTweetResult.Success(action.listItem, mapSentiment(it)) }
          .cast(AnalyzeTweetResult::class.java)
          .onErrorReturn { AnalyzeTweetResult.UnknownError(action.listItem, it) }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(AnalyzeTweetResult.Loading(action.listItem))
    }
  }

  private fun mapSentiment(googleResponse: GoogleAnalyzeResponse): TweetSentiment {
    return googleResponse.documentSentiment?.let {
      when {
        it.score < -0.25 -> SAD
        it.score > 0.25 -> HAPPY
        else -> NEUTRAL
      }
    } ?: NEUTRAL
  }
}
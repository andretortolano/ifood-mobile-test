package sample.study.happytwitter.presentation.usertweets.tweetlist

import io.reactivex.functions.BiFunction
import sample.study.happytwitter.base.viewmodel.reducer.IStateReducer
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.LoadTweetsRequestState
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListResult.AnalyzeTweetResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListResult.LoadTweetsResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListViewState
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.AnalyzeTweetRequestState
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetItemState
import javax.inject.Inject

class TweetListStateReducer @Inject constructor() :
    IStateReducer<TweetListResult, TweetListViewState> {

  override fun reduce(): BiFunction<TweetListViewState, TweetListResult, TweetListViewState> {
    return BiFunction { oldState: TweetListViewState, result: TweetListResult ->
      when (result) {
        is LoadTweetsResult -> when (result) {
          is LoadTweetsResult.Loading -> oldState.copy(loadTweetsRequestState = LoadTweetsRequestState.Loading)
          is LoadTweetsResult.Success -> oldState.copy(loadTweetsRequestState = LoadTweetsRequestState.Success, loadedTweetList = ArrayList(result.tweets.map { TweetItemState(it) }))
          is LoadTweetsResult.PrivateList -> oldState.copy(loadTweetsRequestState = LoadTweetsRequestState.ErrorPrivateTweets)
          is LoadTweetsResult.UnknownError -> oldState.copy(loadTweetsRequestState = LoadTweetsRequestState.Error(result.error))
        }

        is AnalyzeTweetResult -> {
          val newList = arrayListOf<TweetItemState>()
          newList.addAll(oldState.loadedTweetList)
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
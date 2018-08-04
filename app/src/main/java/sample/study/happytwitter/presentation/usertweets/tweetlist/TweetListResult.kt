package sample.study.happytwitter.presentation.usertweets.tweetlist

import sample.study.happytwitter.base.mvi.IResult
import sample.study.happytwitter.data.objects.TwitterTweet
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetItemState
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment

sealed class TweetListResult : IResult {
  sealed class LoadTweetsResult : TweetListResult() {
    object Loading : LoadTweetsResult()
    data class Success(val tweets: List<TwitterTweet>) : LoadTweetsResult()
    object PrivateList : LoadTweetsResult()
    data class UnknownError(val error: Throwable) : LoadTweetsResult()
  }

  sealed class AnalyzeTweetResult(private val item: TweetItemState) : TweetListResult() {
    val tweetId
      get() = item.tweet.id

    data class Loading(val item: TweetItemState) : AnalyzeTweetResult(item)
    data class Success(val item: TweetItemState,
        val sentiment: TweetSentiment
    ) : AnalyzeTweetResult(item)

    data class UnknownError(val item: TweetItemState,
        val error: Throwable
    ) : AnalyzeTweetResult(item)
  }
}
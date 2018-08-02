package sample.study.happytwitter.presentation.usertweets.tweetlist.cycle

import sample.study.happytwitter.base.IIntent
import sample.study.happytwitter.data.objects.TwitterUser
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetItemState

sealed class TweetListIntent : IIntent {
  data class InitIntent(val twitterUser: TwitterUser) : TweetListIntent()
  data class AnalyseTweetIntent(val tweet: TweetItemState) : TweetListIntent()
}
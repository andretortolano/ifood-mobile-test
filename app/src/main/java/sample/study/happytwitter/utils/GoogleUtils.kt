package sample.study.happytwitter.utils

import sample.study.happytwitter.data.google.remote.GoogleAnalyzeResponse
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.HAPPY
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.NEUTRAL
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.SAD

class GoogleUtils {
  companion object {
    fun mapSentiment(googleResponse: GoogleAnalyzeResponse): TweetSentiment {
      return googleResponse.documentSentiment?.let {
        when {
          it.score <= -0.25 -> SAD
          it.score >= 0.25 -> HAPPY
          else -> NEUTRAL
        }
      } ?: NEUTRAL
    }
  }
}
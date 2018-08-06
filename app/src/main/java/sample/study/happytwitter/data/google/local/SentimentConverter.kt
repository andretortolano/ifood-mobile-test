package sample.study.happytwitter.data.google.local

import android.arch.persistence.room.TypeConverter
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.HAPPY
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.NEUTRAL
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.SAD

class SentimentConverter {

  @TypeConverter
  fun sentimentToInt(sentiment: TweetSentiment): Int {
    return when (sentiment) {
      HAPPY -> 1
      NEUTRAL -> 2
      SAD -> 3
    }
  }

  @TypeConverter
  fun intToSentiment(int: Int): TweetSentiment {
    return when (int) {
      1 -> HAPPY
      2 -> NEUTRAL
      3 -> SAD
      else -> NEUTRAL
    }
  }
}
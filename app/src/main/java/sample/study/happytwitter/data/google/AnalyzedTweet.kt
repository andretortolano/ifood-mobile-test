package sample.study.happytwitter.data.google

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment

@Entity(tableName = "analyzedTweet")
data class AnalyzedTweet(
    @PrimaryKey val tweetId: Long,
    val sentiment: TweetSentiment
)
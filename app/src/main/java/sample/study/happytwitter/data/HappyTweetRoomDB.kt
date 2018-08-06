package sample.study.happytwitter.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import sample.study.happytwitter.data.google.AnalyzedTweet
import sample.study.happytwitter.data.google.local.GoogleRoom
import sample.study.happytwitter.data.google.local.SentimentConverter
import sample.study.happytwitter.data.twitter.TwitterTweet
import sample.study.happytwitter.data.twitter.TwitterUser
import sample.study.happytwitter.data.twitter.local.TwitterRoom

@Database(entities = [(TwitterUser::class), (TwitterTweet::class), (AnalyzedTweet::class)], version = 1, exportSchema = false)
@TypeConverters(value = [SentimentConverter::class])
abstract class HappyTweetRoomDB : RoomDatabase() {

  abstract fun twitterRoom(): TwitterRoom

  abstract fun googleRoom(): GoogleRoom
}
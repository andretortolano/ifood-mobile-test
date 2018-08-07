package sample.study.happytwitter.data.twitter.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe
import sample.study.happytwitter.data.twitter.TwitterTweet
import sample.study.happytwitter.data.twitter.TwitterUser

@Dao
interface TwitterRoom {

  @Query("SELECT * FROM user WHERE lower(screen_name) = lower(:screenName)")
  fun getUser(screenName: String): Maybe<TwitterUser>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addUser(twitterUser: TwitterUser)

  @Query("SELECT * FROM tweet WHERE lower(screen_name) = lower(:screenName)")
  fun getUserTweets(screenName: String): Maybe<List<TwitterTweet>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addUserTweets(tweets: List<TwitterTweet>)
}
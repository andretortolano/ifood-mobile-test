package sample.study.happytwitter.data.google.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single
import sample.study.happytwitter.data.google.AnalyzedTweet

@Dao
interface GoogleRoom {

  @Query("SELECT * from analyzedTweet where tweetId = :tweetId")
  fun getAnalyzedTweetById(tweetId: Long): Single<AnalyzedTweet>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addAnalyzedTweet(analyzedTweet: AnalyzedTweet)
}
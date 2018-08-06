package sample.study.happytwitter.data.twitter.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single
import sample.study.happytwitter.data.twitter.TwitterUser

@Dao
interface TwitterRoom {

  @Query("SELECT * from user where screen_name = :screenName")
  fun getUser(screenName: String): Single<TwitterUser>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addUser(twitterUser: TwitterUser)
}
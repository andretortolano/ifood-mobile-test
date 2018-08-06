package sample.study.happytwitter.data.twitter.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import sample.study.happytwitter.data.twitter.TwitterTweet
import sample.study.happytwitter.data.twitter.TwitterUser

interface TwitterAPI {

  @GET("/1.1/users/show.json")
  fun getUser(@Query("screen_name") screenName: String): Single<TwitterUser>

  @GET("/1.1/statuses/user_timeline.json")
  fun getTweetsByUser(@Query("screen_name") screenName: String): Single<List<TwitterTweet>>
}
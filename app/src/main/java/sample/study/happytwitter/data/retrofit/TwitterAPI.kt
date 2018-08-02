package sample.study.happytwitter.data.retrofit

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import sample.study.happytwitter.data.objects.TwitterTweet
import sample.study.happytwitter.data.objects.TwitterUser

interface TwitterAPI {

  @GET("/1.1/users/show.json")
  fun getUser(@Query("screen_name") username: String): Single<TwitterUser>

  @GET("/1.1/statuses/user_timeline.json")
  fun getTweetsByUser(@Query("screen_name") username: String): Single<List<TwitterTweet>>
}
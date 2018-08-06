package sample.study.happytwitter.data.twitter

import io.reactivex.Single

interface ITwitterRepo {

  fun getUser(screenName: String): Single<TwitterUser>

  fun getTweetsByUser(screenName: String): Single<List<TwitterTweet>>
}
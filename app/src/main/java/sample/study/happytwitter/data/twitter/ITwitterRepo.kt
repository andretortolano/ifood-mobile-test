package sample.study.happytwitter.data.twitter

import io.reactivex.Observable

interface ITwitterRepo {

  fun getUser(screenName: String): Observable<TwitterUser>

  fun getTweetsByUser(screenName: String): Observable<List<TwitterTweet>>
}
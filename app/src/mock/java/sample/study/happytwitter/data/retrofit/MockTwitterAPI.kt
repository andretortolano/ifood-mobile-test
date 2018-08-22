package sample.study.happytwitter.data.retrofit

import io.reactivex.Single
import sample.study.happytwitter.data.twitter.TwitterTweet
import sample.study.happytwitter.data.twitter.TwitterUser
import sample.study.happytwitter.data.twitter.remote.TwitterAPI
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class MockTwitterAPI @Inject constructor() : TwitterAPI {

  override fun getUser(screenName: String): Single<TwitterUser> {
    return Single.just(TwitterUser(1, screenName, "MOCK", "mock profile", "00AA55", "https://pbs.twimg.com/profile_banners/180463340/1503946156", "https://pbs.twimg.com/profile_images/901947348699545601/hqRMHITj_400x400.jpg"))
        .delay(3, TimeUnit.SECONDS)
  }

  override fun getTweetsByUser(screenName: String): Single<List<TwitterTweet>> {
    val happyTweet = TwitterTweet(1, "00/09/2002", "Happy message", screenName)
    val sadTweet = TwitterTweet(2, "00/09/2002", "SAD message", screenName)
    val neutralTweet = TwitterTweet(3, "00/09/2002", "neutral message", screenName)
    return Single.just(listOf(happyTweet, sadTweet, neutralTweet))
        .delay(2, SECONDS)
  }
}
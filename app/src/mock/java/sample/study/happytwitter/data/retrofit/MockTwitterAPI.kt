package sample.study.happytwitter.data.retrofit

import io.reactivex.Single
import sample.study.happytwitter.data.objects.TwitterTweet
import sample.study.happytwitter.data.objects.TwitterUser
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MockTwitterAPI @Inject constructor() : TwitterAPI {
  override fun findUser(username: String): Single<TwitterUser> {
    return Single.just(TwitterUser(1, username, "picture"))
      .delay(3, TimeUnit.SECONDS)
  }

  override fun getTwitterPosts(userId: Int): Single<List<TwitterTweet>> {
    return Single.just(arrayListOf(TwitterTweet("message 1"), TwitterTweet("message 1")))
  }
}
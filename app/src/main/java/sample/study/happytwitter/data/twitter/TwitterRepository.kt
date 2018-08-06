package sample.study.happytwitter.data.twitter

import io.reactivex.Single
import sample.study.happytwitter.data.twitter.local.TwitterRoom
import sample.study.happytwitter.data.twitter.remote.TwitterAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterRepository @Inject constructor(
    val local: TwitterRoom,
    val remote: TwitterAPI
) : ITwitterRepo {

  // TODO Persist what is needed into local
  override fun getUser(screenName: String): Single<TwitterUser> {
    return remote.getUser(screenName)
  }

  override fun getTweetsByUser(screenName: String): Single<List<TwitterTweet>> {
    return remote.getTweetsByUser(screenName)
  }
}
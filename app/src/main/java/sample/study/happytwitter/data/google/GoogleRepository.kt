package sample.study.happytwitter.data.google

import io.reactivex.Single
import sample.study.happytwitter.data.google.local.GoogleRoom
import sample.study.happytwitter.data.google.remote.GoogleAPI
import sample.study.happytwitter.data.google.remote.GoogleAnalyzeBody
import sample.study.happytwitter.data.twitter.TwitterTweet
import sample.study.happytwitter.utils.GoogleUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleRepository @Inject constructor(
    val local: GoogleRoom,
    val remote: GoogleAPI
) : IGoogleRepo {

  /**
   * This is a sample of request that does not need to be updated by re-executing the remote call
   *
   * Once this message has a "result" from google Natural Language analysis this result won't change,
   * so its safe to store it locally and never call it again.
   *
   */
  override fun analyzeSentiment(twitterTweet: TwitterTweet): Single<AnalyzedTweet> {
    return local.getAnalyzedTweetById(twitterTweet.id)
        .switchIfEmpty(
          remote.analyzeSentiment(GoogleAnalyzeBody.newInstance(twitterTweet.text))
              .map { AnalyzedTweet(twitterTweet.id, GoogleUtils.mapSentiment(it)) }
              .doOnSuccess(local::addAnalyzedTweet)
        )
  }
}
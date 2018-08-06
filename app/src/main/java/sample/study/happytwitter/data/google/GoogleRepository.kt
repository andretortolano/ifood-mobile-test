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

  // TODO Persist what is needed into local
  override fun analyzeSentiment(twitterTweet: TwitterTweet): Single<AnalyzedTweet> {
    return remote.analyzeSentiment(GoogleAnalyzeBody.newInstance(twitterTweet.text))
        .map { AnalyzedTweet(twitterTweet.id, GoogleUtils.mapSentiment(it)) }
  }
}
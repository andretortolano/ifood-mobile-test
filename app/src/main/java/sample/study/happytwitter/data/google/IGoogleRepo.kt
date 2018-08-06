package sample.study.happytwitter.data.google

import io.reactivex.Single
import sample.study.happytwitter.data.twitter.TwitterTweet

interface IGoogleRepo {

  fun analyzeSentiment(twitterTweet: TwitterTweet): Single<AnalyzedTweet>
}
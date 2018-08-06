package sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem

import sample.study.happytwitter.base.mvi.IViewState
import sample.study.happytwitter.data.twitter.TwitterTweet
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.NEUTRAL

data class TweetItemState(
    val tweet: TwitterTweet,
    val sentiment: TweetSentiment = NEUTRAL,
    val analyzeSentimentRequestState: AnalyzeTweetRequestState? = null
) : IViewState
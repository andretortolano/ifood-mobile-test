package sample.study.happytwitter.presentation.usertweets.tweetlist.cycle

import sample.study.happytwitter.base.IViewState
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetItemState

data class TweetListViewState(
    val loadTweetsRequestState: LoadTweetsRequestState? = null,
    val loadedTweetList: ArrayList<TweetItemState> = arrayListOf()
) : IViewState
package sample.study.happytwitter.presentation.usertweets.tweetlist

import sample.study.happytwitter.base.mvi.IAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetItemState

sealed class TweetListAction : IAction {
  class LoadTweetsAction(val screenName: String) : TweetListAction()
  class AnalyzeTweetAction(val listItem: TweetItemState) : TweetListAction()
}
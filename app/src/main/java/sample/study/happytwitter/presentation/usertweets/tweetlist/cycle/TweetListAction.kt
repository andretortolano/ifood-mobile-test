package sample.study.happytwitter.presentation.usertweets.tweetlist.cycle

import sample.study.happytwitter.base.IAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetItemState

sealed class TweetListAction : IAction {
  class LoadTweetsAction(val screenName: String) : TweetListAction()
  class AnalyzeTweetAction(val listItem: TweetItemState) : TweetListAction()
}
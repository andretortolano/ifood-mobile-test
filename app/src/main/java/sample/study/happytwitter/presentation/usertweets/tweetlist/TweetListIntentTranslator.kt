package sample.study.happytwitter.presentation.usertweets.tweetlist

import sample.study.happytwitter.base.viewmodel.translator.BaseIntentTranslator
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListAction.AnalyzeTweetAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListAction.LoadTweetsAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListIntent
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListIntent.InitIntent
import javax.inject.Inject

class TweetListIntentTranslator @Inject constructor() : BaseIntentTranslator<TweetListIntent, TweetListAction>() {

  override val startIntent: Class<out TweetListIntent>?
    get() = InitIntent::class.java

  override fun actionFromIntent(intent: TweetListIntent): TweetListAction {
    return when (intent) {
      is TweetListIntent.InitIntent -> LoadTweetsAction(intent.twitterUser.screen_name)
      is TweetListIntent.AnalyseTweetIntent -> AnalyzeTweetAction(intent.tweet)
    }
  }
}
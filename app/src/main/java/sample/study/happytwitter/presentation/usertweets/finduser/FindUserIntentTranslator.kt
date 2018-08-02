package sample.study.happytwitter.presentation.usertweets.finduser

import sample.study.happytwitter.base.viewmodel.translator.BaseIntentTranslator
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserAction.ChangeUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserAction.SearchUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserIntent
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserIntent.ChangeUserIntent
import javax.inject.Inject

class FindUserIntentTranslator @Inject constructor() : BaseIntentTranslator<FindUserIntent, FindUserAction>() {

  override val startIntent: Class<out FindUserIntent>?
    get() = null

  override fun actionFromIntent(intent: FindUserIntent): FindUserAction {
    return when (intent) {
      is ChangeUserIntent -> ChangeUserAction(intent.username)
      is FindUserIntent.SearchButtonIntent -> SearchUserAction(intent.username)
      is FindUserIntent.SearchSoftKeyboardIntent -> SearchUserAction(intent.username)
    }
  }
}
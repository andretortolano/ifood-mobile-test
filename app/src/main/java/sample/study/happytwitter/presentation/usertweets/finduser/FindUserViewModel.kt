package sample.study.happytwitter.presentation.usertweets.finduser

import sample.study.happytwitter.base.viewmodel.BaseViewModel
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserAction
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserIntent
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserResult
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserViewState
import javax.inject.Inject

class FindUserViewModel @Inject constructor(
    intentTranslator: FindUserIntentTranslator,
    actionProcessor: FindUserActionProcessor,
    stateReducer: FindUserStateReducer
) : BaseViewModel<FindUserIntent, FindUserAction, FindUserResult, FindUserViewState>
  (intentTranslator, actionProcessor, stateReducer, FindUserViewState())
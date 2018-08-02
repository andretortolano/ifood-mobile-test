package sample.study.happytwitter.presentation.usertweets.tweetlist

import sample.study.happytwitter.base.viewmodel.BaseViewModel
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListAction
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListIntent
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListResult
import sample.study.happytwitter.presentation.usertweets.tweetlist.cycle.TweetListViewState
import javax.inject.Inject

class TweetListViewModel @Inject constructor(
    intentTranslator: TweetListIntentTranslator,
    actionProcessor: TweetListActionProcessor,
    stateReducer: TweetListStateReducer
) : BaseViewModel<TweetListIntent, TweetListAction, TweetListResult, TweetListViewState>
  (intentTranslator, actionProcessor, stateReducer, TweetListViewState())
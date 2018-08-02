package sample.study.happytwitter.base.viewmodel

import io.reactivex.Observable
import sample.study.happytwitter.base.IIntent
import sample.study.happytwitter.base.IViewState

interface IViewModel<I : IIntent, VS : IViewState> {

  /**
   * Handle incoming intents
   */
  fun intentsHandler(intents: Observable<I>)

  /**
   * Emmit States
   */
  fun states(): Observable<VS>
}
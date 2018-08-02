package sample.study.happytwitter.base.view

import io.reactivex.Observable
import sample.study.happytwitter.base.IIntent
import sample.study.happytwitter.base.IViewState

interface IView<I : IIntent, in VS : IViewState> {

  /**
   * Emmit Intents
   */
  fun intents(): Observable<I>

  /**
   * Renders ViewStates
   */
  fun render(state: VS)
}
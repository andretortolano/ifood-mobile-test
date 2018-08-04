package sample.study.happytwitter.base.view

import io.reactivex.Observable
import sample.study.happytwitter.base.mvi.IAction
import sample.study.happytwitter.base.mvi.IViewState

interface IView<A : IAction, in VS : IViewState> {

  /**
   * Emmit Actions
   */
  fun actions(): Observable<A>

  /**
   * Renders ViewStates
   */
  fun render(state: VS)
}
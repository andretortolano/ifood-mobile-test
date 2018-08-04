package sample.study.happytwitter.base.mvi

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

interface IViewModel<A : IAction, R : IResult, VS : IViewState> {

  /**
   * Handle incoming actions
   */
  fun actions(actions: Observable<A>)

  /**
   * Emmit States
   */
  fun states(): Observable<VS>

  /**
   * Action that will emmit only once
   */
  val startAction: Class<out A>?

  /**
   * Mandatory Initial ViewState for each ViewModel cold start
   */
  val initialViewState: VS

  /**
   * Process Action into Result
   */
  fun actionProcessor(): ObservableTransformer<A, R>

  /**
   * From a new Result, change old ViewState into a new ViewState
   */
  fun stateReducer(): BiFunction<VS, R, VS>
}
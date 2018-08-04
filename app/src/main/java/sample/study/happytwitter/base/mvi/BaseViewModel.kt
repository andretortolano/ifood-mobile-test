package sample.study.happytwitter.base.mvi

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import sample.study.happytwitter.utils.notOfType

abstract class BaseViewModel<A : IAction, R : IResult, VS : IViewState> : ViewModel(), IViewModel<A, R, VS> {

  private val actionsSubject: PublishSubject<A> = PublishSubject.create()
  private var statesObservable: Observable<VS>? = null

  private val startActionFilter: ObservableTransformer<A, A>
    get() = if (startAction != null) {
      ObservableTransformer { actions ->
        actions.publish { shared ->
          Observable.merge(
            shared.ofType(startAction!!).take(1),
            shared.notOfType(startAction!!)
          )
        }
      }
    } else {
      ObservableTransformer { actions -> actions.map { it } }
    }

  override fun actions(actions: Observable<A>) {
    actions.subscribe(actionsSubject)
  }

  override fun states(): Observable<VS> {
    if (statesObservable == null) {
      statesObservable = compose()
    }
    return statesObservable!!
  }

  private fun compose(): Observable<VS> {
    return actionsSubject.doOnNext { Log.d("BaseViewModel", "Intent: ${it.javaClass.simpleName}") }
        .compose(startActionFilter)
        .doOnNext { Log.d("BaseViewModel", "Action: ${it.javaClass.simpleName}") }
        .compose(actionProcessor())
        .doOnNext { Log.d("BaseViewModel", "Result: ${it.javaClass.simpleName}") }
        .scan(initialViewState, stateReducer())
        .distinctUntilChanged()
        .doOnNext { Log.d("BaseViewModel", "New ViewState Emitted") }
        .replay(1)
        .autoConnect(0)
  }
}
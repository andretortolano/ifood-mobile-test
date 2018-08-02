package sample.study.happytwitter.base.viewmodel

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import sample.study.happytwitter.base.IAction
import sample.study.happytwitter.base.IIntent
import sample.study.happytwitter.base.IResult
import sample.study.happytwitter.base.IViewState
import sample.study.happytwitter.base.viewmodel.processor.IActionProcessor
import sample.study.happytwitter.base.viewmodel.reducer.IStateReducer
import sample.study.happytwitter.base.viewmodel.translator.IIntentTranslator

abstract class BaseViewModel<I : IIntent, A : IAction, R : IResult, VS : IViewState>(
    private val intentTranslator: IIntentTranslator<I, A>,
    private val actionProcessor: IActionProcessor<A, R>,
    private val stateReducer: IStateReducer<R, VS>,
    private val initialStateView: VS
) : ViewModel(), IViewModel<I, VS> {

  private val intentsSubject: PublishSubject<I> = PublishSubject.create()
  private val statesObservable: Observable<VS> = compose()

  override fun intentsHandler(intents: Observable<I>) {
    intents.subscribe(intentsSubject)
  }

  override fun states(): Observable<VS> =
      statesObservable

  private fun compose(): Observable<VS> {
    return intentsSubject.doOnNext { Log.d("BaseViewModel", "Intent: ${it.javaClass.simpleName}") }
        .compose(intentTranslator.translate())
        .doOnNext { Log.d("BaseViewModel", "Action: ${it.javaClass.simpleName}") }
        .compose(actionProcessor.process())
        .doOnNext { Log.d("BaseViewModel", "Result: ${it.javaClass.simpleName}") }
        .scan(initialStateView, stateReducer.reduce())
        .distinctUntilChanged()
        .doOnNext { Log.d("BaseViewModel", "New ViewState Emitted") }
        .replay(1)
        .autoConnect(0)
  }
}
package sample.study.happytwitter.base.viewmodel.translator

import io.reactivex.ObservableTransformer
import sample.study.happytwitter.base.IAction
import sample.study.happytwitter.base.IIntent

interface IIntentTranslator<I : IIntent, A : IAction> {
  val startIntent: Class<out I>?

  fun actionFromIntent(intent: I): A

  fun translate(): ObservableTransformer<I, A>
}
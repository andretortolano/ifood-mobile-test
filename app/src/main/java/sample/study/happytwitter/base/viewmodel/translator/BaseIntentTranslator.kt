package sample.study.happytwitter.base.viewmodel.translator

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import sample.study.happytwitter.base.IAction
import sample.study.happytwitter.base.IIntent
import sample.study.happytwitter.utils.notOfType

abstract class BaseIntentTranslator<I : IIntent, A : IAction> : IIntentTranslator<I, A> {

  override fun translate(): ObservableTransformer<I, A> {
    return if (startIntent != null) {
      Log.d("ViewModelTranslate", "startIntent $startIntent")
      ObservableTransformer { intents ->
        intents.publish { shared ->
          Observable.merge(
            shared.ofType(startIntent!!).take(1),
            shared.notOfType(startIntent!!)
          )
        }
            .map { actionFromIntent(it) }
      }
    } else {
      Log.d("ViewModelTranslate", "startIntent is null")
      ObservableTransformer { intents ->
        intents.map { actionFromIntent(it) }
      }
    }
  }
}
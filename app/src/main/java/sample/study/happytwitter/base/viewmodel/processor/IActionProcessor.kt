package sample.study.happytwitter.base.viewmodel.processor

import io.reactivex.ObservableTransformer
import sample.study.happytwitter.base.IAction
import sample.study.happytwitter.base.IResult

interface IActionProcessor<A : IAction, R : IResult> {
  fun process(): ObservableTransformer<A, R>
}
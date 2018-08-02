package sample.study.happytwitter.base.viewmodel.reducer

import io.reactivex.functions.BiFunction
import sample.study.happytwitter.base.IResult
import sample.study.happytwitter.base.IViewState

interface IStateReducer<R : IResult, VS : IViewState> {
  fun reduce(): BiFunction<VS, R, VS>
}
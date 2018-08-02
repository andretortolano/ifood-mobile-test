package sample.study.happytwitter.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class MockSchedulerProvider : ISchedulerProvider {
  override fun computation(): Scheduler = Schedulers.trampoline()

  override fun io(): Scheduler = Schedulers.trampoline()

  override fun ui(): Scheduler = Schedulers.trampoline()
}
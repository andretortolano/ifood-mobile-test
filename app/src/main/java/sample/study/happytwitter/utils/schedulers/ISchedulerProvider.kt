package sample.study.happytwitter.utils.schedulers

import io.reactivex.Scheduler

interface ISchedulerProvider {
  fun computation(): Scheduler

  fun io(): Scheduler

  fun ui(): Scheduler
}

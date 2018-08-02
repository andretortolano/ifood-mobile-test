package sample.study.happytwitter.injection

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import sample.study.happytwitter.utils.schedulers.ISchedulerProvider
import sample.study.happytwitter.utils.schedulers.SchedulerProvider

@Suppress("unused")
@Module(includes = [FlavorModule::class])
abstract class AppModule {

  @Binds
  abstract fun bindContext(application: Application): Context

  @Binds
  abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): ISchedulerProvider
}

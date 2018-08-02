package sample.study.happytwitter.injection

import dagger.Binds
import dagger.Module
import sample.study.happytwitter.data.retrofit.MockTwitterAPI
import sample.study.happytwitter.data.retrofit.TwitterAPI
import javax.inject.Singleton

@Suppress("unused")
@Module
abstract class MockRetrofitModule {

  @Binds
  @Singleton
  abstract fun provideTwitterAPI(mock: MockTwitterAPI): TwitterAPI
}
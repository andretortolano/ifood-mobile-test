package sample.study.happytwitter.injection

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sample.study.happytwitter.data.google.remote.GoogleAPI
import sample.study.happytwitter.data.twitter.remote.TwitterAPI
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = ([RetrofitClientModule::class]))
class RetrofitModule {

  @Provides
  @Singleton
  fun provideTwitterAPI(@Named("twitterRetrofit") retrofit: Retrofit): TwitterAPI {
    return retrofit.create(TwitterAPI::class.java)
  }

  @Provides
  @Singleton
  fun provideGoogleAPI(@Named("googleRetrofit") retrofit: Retrofit): GoogleAPI {
    return retrofit.create(GoogleAPI::class.java)
  }
}
package sample.study.happytwitter.injection

import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
class FlavorModule {

  @Provides
  @Singleton
  @Named("twitterBaseUrl")
  fun provideTwitterBaseUrl(): String {
    return "https://api.twitter.com"
  }

  @Provides
  @Singleton
  @Named("googleBaseUrl")
  fun provideGoogleBaseUrl(): String {
    return "https://language.googleapis.com"
  }

}
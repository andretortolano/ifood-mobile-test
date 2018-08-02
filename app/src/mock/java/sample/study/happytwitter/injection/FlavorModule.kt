package sample.study.happytwitter.injection

import dagger.Module

@Suppress("unused")
@Module(includes = [MockRetrofitModule::class])
abstract class FlavorModule {

  //  @Binds
  //  @Singleton
  //  abstract fun providePicassoService(picassoHelper: MockWebHelper): IWebHelper
}
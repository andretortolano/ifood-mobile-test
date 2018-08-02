package sample.study.happytwitter.injection

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitClientModule {

  @Provides
  @Singleton
  fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY
    return logger
  }

  @Provides
  @Singleton
  fun providesRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
    return RxJava2CallAdapterFactory.create()
  }

  @Provides
  @Singleton
  fun provideGsonConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create(GsonBuilder().serializeNulls().create())
  }

  @Provides
  @Named("twitterOkHttpClient")
  @Singleton
  fun providesTwitterOkHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor { chain ->
          val request = chain.request()
              .newBuilder()
              .addHeader("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAALmF6AAAAAAAwiqs3sI41pZgTXjzsN9waYPe%2FQU%3DL1lmYz9DoSlOHzCDNTqtnaEW75mgvp4bf5GE4S9XViYndgYyGB")
              .build()
          chain.proceed(request)
        }
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
  }

  @Provides
  @Named("googleOkHttpClient")
  @Singleton
  fun providesGoogleOkHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(logger)
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
  }

  @Provides
  @Singleton
  @Named("twitterRetrofit")
  fun provideTwitterRetrofit(
      @Named("twitterBaseUrl") baseUrl: String,
      rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
      gsonConverterFactory: GsonConverterFactory,
      @Named("twitterOkHttpClient") okHttpClient: OkHttpClient
  ): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(rxJava2CallAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
  }

  @Provides
  @Singleton
  @Named("googleRetrofit")
  fun provideGoogleRetrofit(
      @Named("googleBaseUrl") baseUrl: String,
      rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
      gsonConverterFactory: GsonConverterFactory,
      @Named("googleOkHttpClient") okHttpClient: OkHttpClient
  ): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(rxJava2CallAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
  }
}
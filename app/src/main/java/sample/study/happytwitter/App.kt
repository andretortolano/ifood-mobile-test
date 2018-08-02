package sample.study.happytwitter

import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import sample.study.happytwitter.injection.DaggerAppComponent

class App : DaggerApplication() {

  override fun onCreate() {
    super.onCreate()

    val picasso = Picasso.Builder(applicationContext)
        .loggingEnabled(true)
        .build()

    Picasso.setSingletonInstance(picasso)
  }

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.builder()
        .application(this)
        .build()
  }
}
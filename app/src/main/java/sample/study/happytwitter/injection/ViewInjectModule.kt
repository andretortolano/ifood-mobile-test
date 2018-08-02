package sample.study.happytwitter.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sample.study.happytwitter.presentation.splash.SplashActivity
import sample.study.happytwitter.presentation.usertweets.UserTweetsActivity
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserFragment
import sample.study.happytwitter.presentation.usertweets.tweetlist.TweetListFragment

@Suppress("unused")
@Module
abstract class ViewInjectModule {

  @ContributesAndroidInjector
  abstract fun injectSplashActivity(): SplashActivity

  @ContributesAndroidInjector
  abstract fun injectUserTweetsActivity(): UserTweetsActivity

  @ContributesAndroidInjector
  abstract fun injectFindUserFragment(): FindUserFragment

  @ContributesAndroidInjector
  abstract fun injectTweetListFragment(): TweetListFragment
}

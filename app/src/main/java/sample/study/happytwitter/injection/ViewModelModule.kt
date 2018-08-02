package sample.study.happytwitter.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sample.study.happytwitter.injection.viewmodel.ViewModelFactory
import sample.study.happytwitter.injection.viewmodel.ViewModelKey
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserViewModel
import sample.study.happytwitter.presentation.usertweets.tweetlist.TweetListViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

  @Binds
  internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(FindUserViewModel::class)
  internal abstract fun bindFindUserViewModel(viewModel: FindUserViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(TweetListViewModel::class)
  internal abstract fun bindTweetListViewModel(viewModel: TweetListViewModel): ViewModel
}
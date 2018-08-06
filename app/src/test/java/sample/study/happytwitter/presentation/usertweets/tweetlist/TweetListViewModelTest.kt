package sample.study.happytwitter.presentation.usertweets.tweetlist

import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import sample.study.happytwitter.base.network.NetworkingViewState
import sample.study.happytwitter.data.google.IGoogleRepo
import sample.study.happytwitter.data.twitter.ITwitterRepo
import sample.study.happytwitter.utils.schedulers.ISchedulerProvider
import sample.study.happytwitter.utils.schedulers.MockSchedulerProvider

@RunWith(MockitoJUnitRunner::class)
class TweetListViewModelTest {

  @Mock
  private lateinit var twitterRepository: ITwitterRepo

  @Mock
  private lateinit var googleRepository: IGoogleRepo

  private lateinit var schedulerProvider: ISchedulerProvider

  private lateinit var viewModel: TweetListViewModel

  private lateinit var testObserver: TestObserver<TweetListViewState>

  private val lastPosition
    get() = testObserver.values().lastIndex

  @Before
  fun before() {
    schedulerProvider = MockSchedulerProvider()

    viewModel = TweetListViewModel(twitterRepository, googleRepository, schedulerProvider)

    testObserver = viewModel.states()
        .test()
  }

  @Test
  fun `Initial ViewState`() {
    testObserver.assertValueAt(lastPosition, TweetListViewState(NetworkingViewState.Idle, null))
  }
}
package sample.study.happytwitter.presentation.usertweets.finduser

import com.nhaarman.mockito_kotlin.any
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import sample.study.happytwitter.data.objects.TwitterUser
import sample.study.happytwitter.data.retrofit.TwitterAPI
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserIntent
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserViewState
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.SearchUserRequestState
import sample.study.happytwitter.utils.schedulers.ISchedulerProvider
import sample.study.happytwitter.utils.schedulers.MockSchedulerProvider

@RunWith(MockitoJUnitRunner::class)
class FindUserViewModelTest {

  @Mock
  private lateinit var twitterAPI: TwitterAPI

  private lateinit var schedulerProvider: ISchedulerProvider

  private lateinit var viewModel: FindUserViewModel

  private lateinit var testObserver: TestObserver<FindUserViewState>

  private val validUser = TwitterUser(1, "valid user", "valid", "valid description", "", "", "")

  @Before
  fun before() {

    schedulerProvider = MockSchedulerProvider()

    val intentTranslator = FindUserIntentTranslator()
    val actionProcessor = FindUserActionProcessor(twitterAPI, schedulerProvider)
    val stateReducer = FindUserStateReducer()
    viewModel = FindUserViewModel(intentTranslator, actionProcessor, stateReducer)

    testObserver = viewModel.states()
        .test()
  }

  @Test
  fun `find user progress`() {
    `when`(twitterAPI.getUser(any())).thenReturn(Single.just(validUser))

    viewModel.intentsHandler(Observable.just(FindUserIntent.SearchButtonIntent("username")))

    testObserver.assertValueAt(1) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.Searching }
    testObserver.assertValueAt(2) { viewState -> viewState.searchUserRequestState != SearchUserRequestState.Searching }
  }

  @Test
  fun `find user Success`() {
    `when`(twitterAPI.getUser(any())).thenReturn(Single.just(validUser))

    viewModel.intentsHandler(Observable.just(FindUserIntent.SearchButtonIntent("username")))

    testObserver.assertValueAt(2) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.Success(validUser) }
  }
}
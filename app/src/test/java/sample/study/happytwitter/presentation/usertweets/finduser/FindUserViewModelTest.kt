package sample.study.happytwitter.presentation.usertweets.finduser

import com.nhaarman.mockito_kotlin.any
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response
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
  fun `Initial ViewState`() {
    testObserver.assertValueAt(0, FindUserViewState(false, null))
  }

  @Test
  fun `SearchUserAction - Verify Searching will emit`() {
    `when`(twitterAPI.getUser(any())).thenReturn(Single.just(validUser))

    viewModel.intentsHandler(Observable.just(FindUserIntent.SearchButtonIntent("username")))

    testObserver.assertValueAt(1) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.Searching }
    testObserver.assertValueAt(2) { viewState -> viewState.searchUserRequestState != SearchUserRequestState.Searching }
  }

  @Test
  fun `SearchUserAction - Success`() {
    `when`(twitterAPI.getUser(any())).thenReturn(Single.just(validUser))

    viewModel.intentsHandler(Observable.just(FindUserIntent.SearchButtonIntent("username")))

    testObserver.assertValueAt(1) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.Searching }
    testObserver.assertValueAt(2) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.Success(validUser) }
  }

  @Test
  fun `SearchUserAction - UserNotFound`() {
    val response =
        Response.error<Void>(404, ResponseBody.create(MediaType.parse(""), "{\"errors\": [{\"code\":50, \"message\": \"User not found.\"}]}"))
    `when`(twitterAPI.getUser(any())).thenReturn(Single.error(HttpException(response)))

    viewModel.intentsHandler(Observable.just(FindUserIntent.SearchButtonIntent("username")))

    testObserver.assertValueAt(1) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.Searching }
    testObserver.assertValueAt(2) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.UserNotFound }
  }

  @Test
  fun `SearchUserAction - UserDisabled`() {
    val response =
        Response.error<Void>(402, ResponseBody.create(MediaType.parse(""), "{\"errors\": [{\"code\":63, \"message\": \"User not found.\"}]}"))
    `when`(twitterAPI.getUser(any())).thenReturn(Single.error(HttpException(response)))

    viewModel.intentsHandler(Observable.just(FindUserIntent.SearchButtonIntent("username")))

    testObserver.assertValueAt(1) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.Searching }
    testObserver.assertValueAt(2) { viewState -> viewState.searchUserRequestState == SearchUserRequestState.UserDisabled }
  }

  @Test
  fun `ChangeUserAction - not empty`() {
    viewModel.intentsHandler(Observable.just(FindUserIntent.ChangeUserIntent("a")))

    testObserver.assertValueAt(1, FindUserViewState(searchEnabled = true))
  }

  @Test
  fun `ChangeUserAction - empty`() {
    viewModel.intentsHandler(Observable.just(FindUserIntent.ChangeUserIntent("")))

    testObserver.assertValueAt(0, FindUserViewState(searchEnabled = false))
  }
}
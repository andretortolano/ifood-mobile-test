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
import sample.study.happytwitter.base.network.NetworkingViewState
import sample.study.happytwitter.base.network.NetworkingViewState.Error
import sample.study.happytwitter.data.twitter.ITwitterRepo
import sample.study.happytwitter.data.twitter.TwitterUser
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserViewState.Companion.ERROR_USER_DISABLED
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserViewState.Companion.ERROR_USER_NOT_FOUND
import sample.study.happytwitter.utils.schedulers.ISchedulerProvider
import sample.study.happytwitter.utils.schedulers.MockSchedulerProvider

@RunWith(MockitoJUnitRunner::class)
class FindUserViewModelTest {

  @Mock
  private lateinit var twitterRepository: ITwitterRepo

  private lateinit var schedulerProvider: ISchedulerProvider

  private lateinit var viewModel: FindUserViewModel

  private lateinit var testObserver: TestObserver<FindUserViewState>

  private val lastPosition
    get() = testObserver.values().lastIndex

  private val validUser = TwitterUser(1, "valid user", "valid", "valid description", "", "", "")

  @Before
  fun before() {
    schedulerProvider = MockSchedulerProvider()

    viewModel = FindUserViewModel(twitterRepository, schedulerProvider)

    testObserver = viewModel.states()
        .test()
  }

  @Test
  fun `Initial ViewState`() {
    testObserver.assertValueAt(lastPosition, FindUserViewState(null, false, NetworkingViewState.Idle))
  }

  @Test
  fun `SearchUserAction - Verify Searching will emit`() {
    `when`(twitterRepository.getUser(any())).thenReturn(Single.just(validUser))

    viewModel.actions(Observable.just(FindUserAction.SearchUserAction("username")))

    testObserver.assertValueAt(lastPosition - 1) { viewState -> viewState.searchUserNetworking == NetworkingViewState.Loading }
    testObserver.assertValueAt(lastPosition) { viewState -> viewState.searchUserNetworking != NetworkingViewState.Loading }
  }

  @Test
  fun `SearchUserAction - Success`() {
    `when`(twitterRepository.getUser(any())).thenReturn(Single.just(validUser))

    viewModel.actions(Observable.just(FindUserAction.SearchUserAction("username")))

    testObserver.assertValueAt(lastPosition, FindUserViewState(validUser, false, NetworkingViewState.Success))
  }

  @Test
  fun `SearchUserAction - UserNotFound`() {
    val response =
        Response.error<Void>(404, ResponseBody.create(MediaType.parse(""), "{\"errors\": [{\"code\":50, \"message\": \"User not found.\"}]}"))
    `when`(twitterRepository.getUser(any())).thenReturn(Single.error(HttpException(response)))

    viewModel.actions(Observable.just(FindUserAction.SearchUserAction("username")))

    testObserver.assertValueAt(lastPosition) { state -> (state.searchUserNetworking as Error).errorMessage == ERROR_USER_NOT_FOUND }
  }

  @Test
  fun `SearchUserAction - UserDisabled`() {
    val response =
        Response.error<Void>(402, ResponseBody.create(MediaType.parse(""), "{\"errors\": [{\"code\":63, \"message\": \"User not found.\"}]}"))
    `when`(twitterRepository.getUser(any())).thenReturn(Single.error(HttpException(response)))

    viewModel.actions(Observable.just(FindUserAction.SearchUserAction("username")))

    testObserver.assertValueAt(lastPosition) { state -> (state.searchUserNetworking as Error).errorMessage == ERROR_USER_DISABLED }
  }

  @Test
  fun `ChangeUserAction - not empty`() {
    viewModel.actions(Observable.just(FindUserAction.ChangeUserAction("a")))

    testObserver.assertValueAt(lastPosition, FindUserViewState(isUserNameValid = true))
  }

  @Test
  fun `ChangeUserAction - empty`() {
    viewModel.actions(Observable.just(FindUserAction.ChangeUserAction("")))

    testObserver.assertValueAt(lastPosition, FindUserViewState(isUserNameValid = false))
  }
}
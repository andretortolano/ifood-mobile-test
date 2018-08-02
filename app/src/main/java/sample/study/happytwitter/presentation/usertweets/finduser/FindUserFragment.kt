package sample.study.happytwitter.presentation.usertweets.finduser

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.editorActionEvents
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.usertweets_finduser_view.loading_progressbar
import kotlinx.android.synthetic.main.usertweets_finduser_view.search_error_textview
import kotlinx.android.synthetic.main.usertweets_finduser_view.search_user_button
import kotlinx.android.synthetic.main.usertweets_finduser_view.username_edittext
import sample.study.happytwitter.R
import sample.study.happytwitter.base.view.BaseFragment
import sample.study.happytwitter.base.view.IView
import sample.study.happytwitter.presentation.usertweets.UserTweetsActivity
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserIntent
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.FindUserViewState
import sample.study.happytwitter.presentation.usertweets.finduser.cycle.SearchUserRequestState
import sample.study.happytwitter.utils.hideSoftKeyboard
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class FindUserFragment : BaseFragment(), IView<FindUserIntent, FindUserViewState> {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: FindUserViewModel by lazy(NONE) {
    ViewModelProviders.of(this, viewModelFactory)[FindUserViewModel::class.java]
  }

  private val disposables = CompositeDisposable()

  private var isSearchEnabled: Boolean = false

  private val username: String
    get() = username_edittext.text.toString()

  override fun onCreateView(inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.usertweets_finduser_view, container, false)
  }

  override fun onViewCreated(view: View,
      savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    // Subscribe to the viewModel and call render for every emitted state
    disposables.add(viewModel.states().subscribe(this::render))

    // Pass the UI's intents to the ViewModel
    viewModel.intentsHandler(intents())
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.clear()
  }

  override fun intents(): Observable<FindUserIntent> {
    val changeUserIntent = username_edittext.textChanges()
        .skipInitialValue()
        .map { FindUserIntent.ChangeUserIntent(username) }

    val searchButtonIntent = search_user_button.clicks()
        .map { FindUserIntent.SearchButtonIntent(username) }

    val searchIconSoftKeyboardIntent = username_edittext.editorActionEvents()
        .filter { it.actionId() == EditorInfo.IME_ACTION_SEARCH && isSearchEnabled }
        .doOnNext { hideSoftKeyboard(activity!!) }
        .map { FindUserIntent.SearchSoftKeyboardIntent(username) }

    return Observable.merge(changeUserIntent, searchButtonIntent, searchIconSoftKeyboardIntent)
  }

  private var searchUserError: Throwable? = null

  override fun render(state: FindUserViewState) {
    isSearchEnabled = state.searchEnabled
    var isLoading = false
    var searchErrorMessage: String? = null
    var error: Throwable? = null
    state.searchUserRequestState?.let { requestState ->
      when (requestState) {
        is SearchUserRequestState.Searching -> isLoading = true
        is SearchUserRequestState.Success -> {
          (activity as UserTweetsActivity).showTweetListView(requestState.twitterUser)
          return
        }
        is SearchUserRequestState.UserNotFound -> searchErrorMessage = getString(R.string.usertweets_finduser_error_user_not_found)
        is SearchUserRequestState.UserDisabled -> searchErrorMessage = getString(R.string.usertweets_finduser_error_user_disabled)
        is SearchUserRequestState.Error -> error = requestState.error
      }
    }

    loading_progressbar.visibility = if (isLoading) {
      View.VISIBLE
    } else {
      View.GONE
    }

    search_user_button.isEnabled = isSearchEnabled && !isLoading

    if (searchErrorMessage != null) {
      search_error_textview.text = searchErrorMessage
      search_error_textview.visibility = View.VISIBLE
    } else {
      search_error_textview.visibility = View.GONE
    }

    if (searchUserError != error) {
      searchUserError = error
      showToast(searchUserError)
    }
  }
}
package sample.study.happytwitter.presentation.usertweets

import android.os.Bundle
import kotlinx.android.synthetic.main.usertweets_view.primary_fragment
import sample.study.happytwitter.R
import sample.study.happytwitter.base.view.BaseActivity
import sample.study.happytwitter.data.twitter.TwitterUser
import sample.study.happytwitter.presentation.usertweets.finduser.FindUserFragment
import sample.study.happytwitter.presentation.usertweets.tweetlist.TweetListFragment
import sample.study.happytwitter.utils.addFragmentToActivity
import sample.study.happytwitter.utils.hideSoftKeyboard

class UserTweetsActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.usertweets_view)

    if (savedInstanceState == null) {
      showFindUserView()
    }
  }

  override fun onBackPressed() {
    val firstFragment = supportFragmentManager.findFragmentByTag(FindUserFragment::class.java.simpleName)
    if (firstFragment != null && firstFragment.isVisible) {
      super.onBackPressed()
    } else {
      showFindUserView()
    }
  }

  fun showFindUserView() {
    addFragmentToActivity(supportFragmentManager, FindUserFragment(), primary_fragment.id)
    hideSoftKeyboard(this)
  }

  fun showTweetListView(twitterUser: TwitterUser) {
    addFragmentToActivity(supportFragmentManager, TweetListFragment.newInstance(twitterUser), primary_fragment.id)
    hideSoftKeyboard(this)
  }
}
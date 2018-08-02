package sample.study.happytwitter.presentation.splash

import android.content.Intent
import android.os.Bundle
import sample.study.happytwitter.base.view.BaseActivity
import sample.study.happytwitter.presentation.usertweets.UserTweetsActivity

class SplashActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    val intent = Intent(this, UserTweetsActivity::class.java)
    startActivity(intent)
    finish()
  }
}
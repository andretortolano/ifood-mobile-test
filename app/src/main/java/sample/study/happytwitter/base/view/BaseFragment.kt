package sample.study.happytwitter.base.view

import android.widget.Toast
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {

  protected fun showToast(error: String?) {
    error?.let {
      Toast.makeText(activity, it, Toast.LENGTH_LONG)
          .show()
    }
  }
}
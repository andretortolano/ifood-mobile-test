package sample.study.happytwitter.base.view

import android.widget.Toast
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {

  protected fun showToast(error: Throwable?) {
    error?.let {
      Toast.makeText(activity, it.message, Toast.LENGTH_LONG)
          .show()
    }
  }
}
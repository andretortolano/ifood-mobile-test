package sample.study.happytwitter.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
@SuppressLint("CommitTransaction")
fun addFragmentToActivity(
    fragmentManager: FragmentManager,
    fragment: Fragment,
    frameId: Int
) {
  fragmentManager.beginTransaction()
      .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
      .replace(frameId, fragment, fragment::class.java.simpleName)
      .commit()
}

fun hideSoftKeyboard(activity: Activity) {
  var view = activity.currentFocus
  if (view == null) {
    view = View(activity)
  }

  val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(view.windowToken, 0)
}
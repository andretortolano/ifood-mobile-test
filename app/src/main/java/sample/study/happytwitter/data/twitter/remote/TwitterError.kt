package sample.study.happytwitter.data.twitter.remote

import com.google.gson.Gson
import retrofit2.HttpException

class TwitterError constructor(response: HttpException) {
  var code: Int = -1
  var message: String = ""

  init {
    val errorJsonString = response.response()
        .errorBody()
        ?.string()

    val responseError = Gson().fromJson(errorJsonString, TwitterErrorList::class.java)
    responseError.errors?.let { TwitterErrorItem ->
      TwitterErrorItem.firstOrNull()
          .let {
            code = it?.code ?: 0
            message = it?.message ?: ""
          }
    }
  }

  data class TwitterErrorList(
      val errors: List<TwitterErrorItem>?
  )

  data class TwitterErrorItem(
      val code: Int?,
      val message: String?
  )
}
package sample.study.happytwitter.data.google.remote

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleAPI {

  companion object {
    const val KEY = "AIzaSyDbdNLe4G5GiUIZ66ZCHn_BrUTs0fu48XA"
  }

  @POST("/v1/documents:analyzeSentiment?key=$KEY")
  fun analyzeSentiment(@Body body: GoogleAnalyzeBody): Single<GoogleAnalyzeResponse>
}
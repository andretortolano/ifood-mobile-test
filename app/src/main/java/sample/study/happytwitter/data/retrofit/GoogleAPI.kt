package sample.study.happytwitter.data.retrofit

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import sample.study.happytwitter.data.objects.GoogleAnalyzeBody
import sample.study.happytwitter.data.objects.GoogleAnalyzeResponse

interface GoogleAPI {

  companion object {
    const val KEY = "AIzaSyDbdNLe4G5GiUIZ66ZCHn_BrUTs0fu48XA"
  }

  @POST("/v1/documents:analyzeSentiment?key=$KEY")
  fun analyzeSentiment(@Body body: GoogleAnalyzeBody): Single<GoogleAnalyzeResponse>
}
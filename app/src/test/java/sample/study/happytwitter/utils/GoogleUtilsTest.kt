package sample.study.happytwitter.utils

import org.junit.Test
import sample.study.happytwitter.data.google.remote.GoogleAnalyzeResponse
import sample.study.happytwitter.data.google.remote.GoogleAnalyzeResponse.DocumentSentiment
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.HAPPY
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.NEUTRAL
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetSentiment.SAD

class GoogleUtilsTest {

  @Test
  fun `mapSentiment happy`() {
    val googleResponse = GoogleAnalyzeResponse(DocumentSentiment(0.25))
    val result = GoogleUtils.mapSentiment(googleResponse)

    assert(result == HAPPY)
  }

  @Test
  fun `mapSentiment bad`() {
    val googleResponse = GoogleAnalyzeResponse(DocumentSentiment(-0.25))
    val result = GoogleUtils.mapSentiment(googleResponse)

    assert(result == SAD)
  }

  @Test
  fun `mapSentiment neutral`() {
    val googleResponse = GoogleAnalyzeResponse(DocumentSentiment(0.0))
    val result = GoogleUtils.mapSentiment(googleResponse)

    assert(result == NEUTRAL)
  }
}
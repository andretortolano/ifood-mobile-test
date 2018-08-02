package sample.study.happytwitter.data.objects

data class GoogleAnalyzeBody(val document: Document,
    val encodingType: String = "UTF8"
) {
  data class Document(val content: String,
      val type: String = "PLAIN_TEXT"
  )

  companion object {
    fun newInstance(content: String): GoogleAnalyzeBody {
      return GoogleAnalyzeBody(Document(content))
    }
  }
}


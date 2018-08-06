package sample.study.happytwitter.data.google.remote

data class GoogleAnalyzeResponse(val documentSentiment: DocumentSentiment?) {
  data class DocumentSentiment(val score: Double)
}


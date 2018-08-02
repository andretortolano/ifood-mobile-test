package sample.study.happytwitter.data.objects

data class GoogleAnalyzeResponse(val documentSentiment: DocumentSentiment?) {
  data class DocumentSentiment(val score: Double)
}


package sample.study.happytwitter.base.network

sealed class NetworkingViewState {
  object Idle : NetworkingViewState()
  object Loading : NetworkingViewState()
  object Success : NetworkingViewState()
  class Error(val errorMessage: String?) : NetworkingViewState()
}
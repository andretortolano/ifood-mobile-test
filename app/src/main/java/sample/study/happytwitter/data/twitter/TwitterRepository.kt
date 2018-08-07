package sample.study.happytwitter.data.twitter

import android.util.Log
import io.reactivex.Observable
import sample.study.happytwitter.data.twitter.local.TwitterRoom
import sample.study.happytwitter.data.twitter.remote.TwitterAPI
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterRepository @Inject constructor(
    val local: TwitterRoom,
    val remote: TwitterAPI
) : ITwitterRepo {

  override fun getUser(screenName: String): Observable<TwitterUser> {
    return Observable.concat(getUserLocal(screenName), getUserRemote(screenName))
        .debounce(500, MILLISECONDS)
        .distinctUntilChanged()
        .doOnNext { Log.d("ViewModelRepository", "User emmit for screenName = $screenName | $it") }
  }

  private fun getUserLocal(screenName: String): Observable<TwitterUser> {
    return local.getUser(screenName)
        .toObservable()
        .doOnNext { Log.d("ViewModelRepository", "info came from local") }
  }

  private fun getUserRemote(screenName: String): Observable<TwitterUser> {
    return remote.getUser(screenName)
        .doOnSuccess(local::addUser)
        .toObservable()
        .doOnNext { Log.d("ViewModelRepository", "user loaded from web and saved into Local") }
  }

  override fun getTweetsByUser(screenName: String): Observable<List<TwitterTweet>> {
    return Observable.concat(getTweetsByUserLocal(screenName), getTweetsByUserRemote(screenName))
        .debounce(1000, MILLISECONDS)
        .distinctUntilChanged()
        .doOnNext { Log.d("ViewModelRepository", "Tweets emmit for screenName = $screenName | size = ${it.size}") }
  }

  private fun getTweetsByUserLocal(screenName: String): Observable<List<TwitterTweet>> {
    return local.getUserTweets(screenName)
        .toObservable()
        .doOnNext { Log.d("ViewModelRepository", "Tweets Loaded from Local repository") }
  }

  private fun getTweetsByUserRemote(screenName: String): Observable<List<TwitterTweet>> {
    return remote.getTweetsByUser(screenName)
        .map { success ->
          success.forEach { it.screen_name = screenName }
          success
        }
        .doOnSuccess(local::addUserTweets)
        .toObservable()
        .doOnNext { Log.d("ViewModelRepository", "Tweets Loaded from Web") }
  }
}
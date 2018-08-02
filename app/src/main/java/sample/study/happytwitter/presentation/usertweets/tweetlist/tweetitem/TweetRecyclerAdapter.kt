package sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem

import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.usertweets_tweetlist_item_view.view.loading_progressbar
import kotlinx.android.synthetic.main.usertweets_tweetlist_item_view.view.tweet_sentiment_imageview
import kotlinx.android.synthetic.main.usertweets_tweetlist_item_view.view.tweet_textview
import sample.study.happytwitter.R
import sample.study.happytwitter.presentation.usertweets.tweetlist.tweetitem.TweetRecyclerAdapter.ViewHolder

class TweetRecyclerAdapter : RecyclerView.Adapter<ViewHolder>() {

  private val itemClickSubject: PublishSubject<TweetItemState> = PublishSubject.create()

  val itemClickObservable: Observable<TweetItemState>
    get() = itemClickSubject

  private var adapterList: List<TweetItemState> = listOf()

  class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

  override fun onCreateViewHolder(parent: ViewGroup,
      viewType: Int
  ): ViewHolder =
      ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.usertweets_tweetlist_item_view, parent, false))

  override fun getItemCount(): Int =
      adapterList.size

  override fun onBindViewHolder(holder: ViewHolder,
      position: Int
  ) {
    val view = holder.itemView
    val state = adapterList[position]

    view.tweet_textview.text = state.tweet.text
    view.setOnClickListener(null)

    state.analyzeSentimentRequestState?.let {
      when (it) {
        is AnalyzeTweetRequestState.Loading -> {
          view.background = ContextCompat.getDrawable(view.context, R.drawable.tweetlist_item_bg)
          view.tweet_sentiment_imageview.visibility = View.GONE
          view.loading_progressbar.visibility = View.VISIBLE
        }
        is AnalyzeTweetRequestState.Success -> {
          when (state.sentiment) {
            TweetSentiment.HAPPY -> {
              view.background = ContextCompat.getDrawable(view.context, R.drawable.tweetlist_item_happy_bg)
              view.tweet_sentiment_imageview.setImageResource(R.drawable.sentiment_happy_icon)
            }
            TweetSentiment.NEUTRAL -> {
              view.background = ContextCompat.getDrawable(view.context, R.drawable.tweetlist_item_neutral_bg)
              view.tweet_sentiment_imageview.setImageResource(R.drawable.sentiment_neutral_icon)
            }
            TweetSentiment.SAD -> {
              view.background = ContextCompat.getDrawable(view.context, R.drawable.tweetlist_item_sad_bg)
              view.tweet_sentiment_imageview.setImageResource(R.drawable.sentiment_sad_icon)
            }
          }
          view.tweet_sentiment_imageview.visibility = View.VISIBLE
          view.loading_progressbar.visibility = View.GONE
        }
        is AnalyzeTweetRequestState.Error -> {
          renderInitialState(view, state)
        }
      }
    } ?: run {
      renderInitialState(view, state)
    }
  }

  private fun renderInitialState(view: View,
      state: TweetItemState
  ) {
    view.background = ContextCompat.getDrawable(view.context, R.drawable.tweetlist_item_bg)
    view.tweet_sentiment_imageview.setImageResource(R.drawable.question_mark_icon)
    view.tweet_sentiment_imageview.visibility = View.VISIBLE
    view.loading_progressbar.visibility = View.GONE
    view.setOnClickListener {
      itemClickSubject.onNext(state)
    }
  }

  fun refreshList(list: List<TweetItemState>) {
    val beforeItems = adapterList
    adapterList = list
    if (beforeItems.isEmpty()) {
      notifyDataSetChanged()
    } else {
      val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize(): Int =
            beforeItems.size

        override fun getNewListSize(): Int =
            adapterList.size

        override fun areItemsTheSame(oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
          return beforeItems[oldItemPosition].tweet == adapterList[newItemPosition].tweet
        }

        override fun areContentsTheSame(oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
          return beforeItems[oldItemPosition] == adapterList[newItemPosition]
        }
      })
      diffResult.dispatchUpdatesTo(this)
    }
  }
}
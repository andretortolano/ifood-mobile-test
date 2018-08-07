package sample.study.happytwitter.data.twitter

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tweet")
data class TwitterTweet(
    @PrimaryKey val id: Long,
    val created_at: String,
    val text: String,
    var screen_name: String?
)
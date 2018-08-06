package sample.study.happytwitter.injection

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import sample.study.happytwitter.data.HappyTweetRoomDB
import sample.study.happytwitter.data.google.local.GoogleRoom
import sample.study.happytwitter.data.twitter.local.TwitterRoom
import javax.inject.Singleton

@Module
class RoomModule {

  @Provides
  @Singleton
  fun provideAppDatabase(context: Context): HappyTweetRoomDB {
    return Room.databaseBuilder(context, HappyTweetRoomDB::class.java, "happy-tweet-db")
        .build()
  }

  @Provides
  @Singleton
  fun provideGoogleRoom(db: HappyTweetRoomDB): GoogleRoom {
    return db.googleRoom()
  }

  @Provides
  @Singleton
  fun provideTwitterRoom(db: HappyTweetRoomDB): TwitterRoom {
    return db.twitterRoom()
  }
}
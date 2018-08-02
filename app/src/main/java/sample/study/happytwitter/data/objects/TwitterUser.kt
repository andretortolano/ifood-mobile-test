package sample.study.happytwitter.data.objects

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

data class TwitterUser(val id: Long,
    val name: String,
    val screen_name: String,
    val description: String,
    val profile_link_color: String,
    val profile_banner_url: String?,
    val profile_image_url: String?
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readLong(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString())

  override fun writeToParcel(parcel: Parcel,
      flags: Int
  ) {
    parcel.writeLong(id)
    parcel.writeString(name)
    parcel.writeString(screen_name)
    parcel.writeString(description)
    parcel.writeString(profile_link_color)
    parcel.writeString(profile_image_url)
    parcel.writeString(profile_banner_url)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<TwitterUser> {
    override fun createFromParcel(parcel: Parcel): TwitterUser {
      return TwitterUser(parcel)
    }

    override fun newArray(size: Int): Array<TwitterUser?> {
      return arrayOfNulls(size)
    }
  }
}
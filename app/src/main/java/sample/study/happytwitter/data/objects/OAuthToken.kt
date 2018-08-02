package sample.study.happytwitter.data.objects

data class OAuthToken(
    val access_token: String,
    val refresh_token: String? = null,
    val expires_in: Long = 0,
    val token_type: String? = null
)
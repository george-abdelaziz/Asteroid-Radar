package com.example.android.asteroidradar.api

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.asteroidradar.database.AsteroidDao
import com.example.android.asteroidradar.parseAsteroidsJsonResult
import com.example.android.asteroidradar.utils.Constants
import com.example.android.pictureOfDayradar.database.PictureDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

enum class NasaApiStatus { LOADING, ERROR, DONE }

class AsteroidRepository(
    val pictureDatabse: PictureDao,
    val database: AsteroidDao,
    val retrofitService: NasaApiServices) {

    var imageStatus = MutableLiveData<NasaApiStatus>()
    var asteroidStatus = MutableLiveData<NasaApiStatus>()

    suspend fun onStarUp() {
        getImage()
        getAsteroids()
    }

    suspend fun getImage() {
        withContext(Dispatchers.IO) {
            var deferredImage = NasaApi.retrofitService.getImage(Constants.KEY)
            try {
                imageStatus.postValue(NasaApiStatus.LOADING)
                Log.i("qwerty repo image", "loading")
                var image = deferredImage.await()
                pictureDatabse.insertPhoto(image)
                imageStatus.postValue(NasaApiStatus.DONE)
                Log.i("qwerty repo image", "Done")
            } catch (t: Throwable) {
                imageStatus.postValue(NasaApiStatus.ERROR)
                Log.i("qwerty repo image", "ERROR")
            }
        }
    }

    suspend fun getAsteroids() {
        withContext(Dispatchers.IO) {
            val currentTime = Calendar.getInstance().time
            val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            } else {
                TODO("VERSION.SDK_INT < N")
            }
            var lol = dateFormat.format(currentTime)
            val deferredJsonObject = retrofitService.getItems(lol, Constants.KEY)
            try {
                asteroidStatus.postValue(NasaApiStatus.LOADING)
                Log.i("qwerty repo asteroid", "loading")
                var jsonObject = deferredJsonObject.await()
                database.insertAll(parseAsteroidsJsonResult(JSONObject(jsonObject)))
                asteroidStatus.postValue(NasaApiStatus.DONE)
                Log.i("qwerty repo asteroid", "done")
            } catch (t: Throwable) {
                asteroidStatus.postValue(NasaApiStatus.ERROR)
                Log.i("qwerty repo asteroid", "error")

            }
        }
    }
}

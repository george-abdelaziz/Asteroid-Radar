package com.example.android.asteroidradar.all_fragment

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.asteroidradar.api.AsteroidRepository
import com.example.android.asteroidradar.database.Asteroid
import com.example.android.asteroidradar.database.AsteroidDao
import com.example.android.asteroidradar.database.PictureOfDay
import com.example.android.asteroidradar.utils.Constants
import com.example.android.pictureOfDayradar.database.PictureDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListViewModel(
    val pictureDatabse: PictureDao,
    val database: AsteroidDao,
    val reprository: AsteroidRepository,
    application: Application
) : AndroidViewModel(application) {

    lateinit var today: String
    lateinit var nextWeek: String
    lateinit var yesterday: String
    lateinit var tomorow: String

    lateinit var asteroidList1:List<Asteroid>
    lateinit var asteroidList2:List<Asteroid>

    var todayLong: Long = 0
    var yesterdayLong: Long = 0
    var tomorowLong: Long = 0
    var nextWeekLong: Long = 0

    init {
        onStart()
    }

    var asteroidList = database.getAll()
    private val adapterScope = CoroutineScope(Dispatchers.Default)



    val picture: LiveData<PictureOfDay?> = pictureDatabse.getAll()

    private val _clickedItemID = MutableLiveData<Long?>()
    val clickedItemID
        get() = _clickedItemID

    fun onItemClicked(id: Long) {
        _clickedItemID.value = id
    }

    fun onNavigation() {
        _clickedItemID.value = null
    }

    fun onStart() {
        viewModelScope.launch(Dispatchers.IO) {
            reprository.onStarUp()
            asteroidList1 = database.filteredList(yesterdayLong, tomorowLong)
            asteroidList2 = database.filteredList(todayLong, nextWeekLong)

        }
        var calendar = Calendar.getInstance()
        var currentTime = calendar.time
        var dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }

        today = dateFormat.format(currentTime)
        todayLong = conv(today)

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        currentTime = calendar.time
        yesterday = dateFormat.format(currentTime)
        yesterdayLong = conv(yesterday)

        calendar.add(Calendar.DAY_OF_YEAR, 2)
        currentTime = calendar.time
        tomorow = dateFormat.format(currentTime)
        tomorowLong = conv(tomorow)

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        currentTime = calendar.time
        nextWeek = dateFormat.format(currentTime)
        nextWeekLong = conv(nextWeek)
    }

    fun conv(s:String):Long{
        var l:Long = 0
        s.forEach {
            if(it != '-'){
                l = l + it.digitToInt()
                l = 10*l
            }
        }
        return l
    }
}

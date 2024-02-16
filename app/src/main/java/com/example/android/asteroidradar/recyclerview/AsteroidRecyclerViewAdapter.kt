package com.example.android.asteroidradar.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.asteroidradar.database.Asteroid
import com.example.android.asteroidradar.database.PictureOfDay
import com.example.android.asteroidradar.databinding.HeaderImageBinding
import com.example.android.asteroidradar.databinding.ItemLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class AsteroidRecyclerViewAdapter(
    val picture: LiveData<PictureOfDay?>,
    val AsteroidList: LiveData<List<Asteroid>>,
    val clickListener: ItemListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>
    (AsteroidDiffCallback()) {
    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    private val adapterScope = CoroutineScope((Dispatchers.Default))

    fun addHeaderAndSubmitList(pic: PictureOfDay?, list: List<Asteroid>) {
        adapterScope.launch {
            var items = listOf<DataItem>()
            if ((pic?.media_type == "image") && (list != null)) {
                items = listOf(DataItem.Header(pic)) + list.map { DataItem.AsteroidItem(it) }
            } else if (pic?.media_type != "image" && list != null) {
                items = list.map { DataItem.AsteroidItem(it) }
            } else if (pic?.media_type != "image" && list == null) {
                items = listOf(DataItem.Header(pic!!))
            } else {
                items = listOf<DataItem>()
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> ImageViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> AsteroidViewHolder.from(parent)
            else -> throw java.lang.ClassCastException("Unknnown viewType${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AsteroidViewHolder -> {
                val asteroidItem = getItem(position) as DataItem.AsteroidItem
                holder.bind(asteroidItem.asteroid, clickListener)
            }
            is ImageViewHolder -> {
                val imageItem = getItem(position) as DataItem.Header
                holder.bind(imageItem.pic)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.AsteroidItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ImageViewHolder(val binding: HeaderImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PictureOfDay?) {
            binding.pic = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderImageBinding.inflate(layoutInflater, parent, false)
                return ImageViewHolder(binding)
            }
        }
    }

    class AsteroidViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Asteroid, clickListener: ItemListener) {
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLayoutBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }
}

class AsteroidDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class ItemListener(val clickListener: (clickedItemId: Long) -> Unit) {
    fun onClick(Asteroid: Asteroid) = clickListener(Asteroid.id)
}

sealed class DataItem {
    abstract val id: Long

    data class AsteroidItem(val asteroid: Asteroid) : DataItem() {
        override val id: Long
            get() = asteroid.id
    }

    data class Header(val pic: PictureOfDay) : DataItem() {
        override val id: Long = Long.MIN_VALUE
    }
}

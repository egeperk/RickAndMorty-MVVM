package com.egeperk.projedigieggs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.egeperk.projedigieggs.CharactersQuery
import com.egeperk.projedigieggs.R
import com.egeperk.projedigieggs.databinding.CharRowBinding
import com.squareup.picasso.Picasso

class ItemAdapter :
    ListAdapter<CharactersQuery.Result, CharacterViewHolder>(CharacterDiffUtil()) {


    var onEndOfListReached: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding: CharRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.char_row, parent, false
        )

        return CharacterViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.binding.character = getItem(position)


        if (position == itemCount - 1) {
            onEndOfListReached?.invoke()
        }

    }



}



class CharacterViewHolder(val binding: CharRowBinding) : RecyclerView.ViewHolder(binding.root)

class CharacterDiffUtil : DiffUtil.ItemCallback<CharactersQuery.Result>() {

    override fun areItemsTheSame(
        oldItem: CharactersQuery.Result,
        newItem: CharactersQuery.Result
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharactersQuery.Result,
        newItem: CharactersQuery.Result
    ): Boolean {
        return oldItem == newItem
    }

}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    imageView.load(url) { crossfade(true) }
}
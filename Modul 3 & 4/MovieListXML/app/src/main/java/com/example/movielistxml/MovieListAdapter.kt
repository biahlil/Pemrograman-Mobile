package com.example.movielistxml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movielistxml.databinding.ItemMovieBinding
import com.example.movielistxml.domain.MovieModel

class MovieListAdapter(
    private val onDetailClick: (MovieModel) -> Unit,
    private val onBrowserClick: (MovieModel) -> Unit
) : ListAdapter<MovieModel, MovieListAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieModel) {
            val context = binding.root.context
            val titleText = getString(context, movie.titleText)
            val yearText = getString(context, movie.yearText)
            val descriptionText = getString(context, movie.descriptionText)

            binding.ivMoviePoster.setImageResource(movie.image)
            binding.tvMovieTitle.text = titleText
            binding.tvMovieYear.text = yearText
            binding.tvMovieDescription.text = descriptionText
            binding.btnItemDetail.setOnClickListener {
                onDetailClick(movie)
            }

            binding.btnItemBrowser.setOnClickListener {
                onBrowserClick(movie)
            }
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem == newItem
    }
}

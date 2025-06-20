package com.example.movielistxml.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movielistxml.data.repository.MovieRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.movielistxml.data.repository.impl.FakeMovieRepository
import com.example.movielistxml.databinding.FragmentMovieDetailBinding
import timber.log.Timber
import kotlin.getValue

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels {
        MovieDetailViewModelFactory(FakeMovieRepository())
    }

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        Timber.tag("MovieDetailFragment").d("onCreateView: Binding inflated.")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("MovieDetailFragment").d("onViewCreated: View is created.")
        viewModel.getMovieDetail(args.movieId)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            movie?.let {
                Timber.tag("MovieDetailFragment").i("Observing movies: $movie items submitted to adapter.")
                binding.ivDetailPoster.setImageResource(it.image)
                binding.tvDetailTitle.text = getString(it.titleText)
                binding.tvDetailYear.text = getString(it.yearText)
                binding.tvDetailDescription.text = getString(it.descriptionText)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Timber.tag("MovieListFragment").d("Observing isLoading: $isLoading")
            binding.detailProgressBar.isVisible = isLoading

            binding.contentScrollView.isVisible = !isLoading
            Timber.tag("MovieListFragment").d("Making rvMovies invisible")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class MovieDetailViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

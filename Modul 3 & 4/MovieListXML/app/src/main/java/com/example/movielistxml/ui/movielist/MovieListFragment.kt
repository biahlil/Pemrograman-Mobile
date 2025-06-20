package com.example.movielistxml.ui.movielist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.movielistxml.MovieListAdapter
import com.example.movielistxml.data.repository.MovieRepository
import com.example.movielistxml.data.repository.impl.FakeMovieRepository
import com.example.movielistxml.databinding.FragmentMovieListBinding
import timber.log.Timber

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieListViewModel by viewModels {
        MovieListViewModelFactory(FakeMovieRepository())
    }

    private lateinit var movieAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        Timber.tag("MovieListFragment").d("onCreateView: Binding inflated.")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("MovieListFragment").d("onViewCreated: View is created.")

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieListAdapter(
            onDetailClick = { movie ->
                // Logika untuk tombol Detail
                Timber.d("Detail button clicked for: ${movie.titleText}")
                val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie.id)
                findNavController().navigate(action)
            },
            onBrowserClick = { movie ->
                Timber.d("Browser button clicked for: ${movie.titleText}")
                val imdbUrl = getString(movie.imdbLink)
                val intent = Intent(Intent.ACTION_VIEW, imdbUrl.toUri())
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.rvMovies.adapter = movieAdapter
        Timber.tag("MovieListFragment").d("setupRecyclerView: RecyclerView and Adapter are set up.")
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isNotEmpty()) {
                Timber.tag("MovieListFragment").i("Observing movies: ${movies.size} items submitted to adapter.")
                movieAdapter.submitList(movies)
                binding.rvMovies.isVisible = movies.isNotEmpty()
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Timber.tag("MovieListFragment").d("Observing isLoading: $isLoading")
            binding.listProgressBar.isVisible = isLoading
            if (isLoading) {
                binding.rvMovies.isVisible = false
                Timber.tag("MovieListFragment").d("Making rvMovies invisible")

            }
        }
        Timber.tag("MovieListFragment").d("observeViewModel: Observers are set.")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Timber.tag("MovieListFragment").d("onDestroyView: Binding is now null.")
    }
}

class MovieListViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



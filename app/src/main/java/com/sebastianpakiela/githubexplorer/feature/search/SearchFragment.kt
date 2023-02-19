package com.sebastianpakiela.githubexplorer.feature.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sebastianpakiela.githubexplorer.R
import com.sebastianpakiela.githubexplorer.base.BaseFragment
import com.sebastianpakiela.githubexplorer.databinding.FragmentSearchBinding
import com.sebastianpakiela.githubexplorer.di.ViewModelFactory
import com.sebastianpakiela.githubexplorer.domain.usecase.UserAndRepoValidationStatus
import com.sebastianpakiela.githubexplorer.extension.collectWhileStarted
import com.sebastianpakiela.githubexplorer.feature.search.recentlyviewed.RecentlyViewedReposAdapter
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: SearchViewModel

    private lateinit var recentlyViewedReposAdapter: RecentlyViewedReposAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchCta.setOnClickListener {
            viewModel.dispatchAction(ValidateInput(binding.searchInput.text.toString()))
        }
        recentlyViewedReposAdapter = RecentlyViewedReposAdapter {
            viewModel.dispatchAction(ValidateInput(it))
        }
        binding.recentlyViewedRecyclerView.adapter = recentlyViewedReposAdapter

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorFlow.collectWhileStarted(viewLifecycleOwner) {
            binding.searchInputLayout.error = getString(it.toStringResourceMessage())
        }

        viewModel.loadingFlow.collectWhileStarted(viewLifecycleOwner) {
            binding.progressIndicator.isVisible = it
        }

        viewModel.goToDetailsFlow.collectWhileStarted(viewLifecycleOwner) {
            findNavController().navigate(SearchFragmentDirections.actionSearchToDetails(it))
        }

        viewModel.goToErrorSnackBarFlow.collectWhileStarted(viewLifecycleOwner) {
            Snackbar.make(binding.root, R.string.get_repo_error, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.recentlyViewedRepositoriesFlow.collectWhileStarted(viewLifecycleOwner) {
            binding.recentlyViewedGroup.isVisible = it.isNotEmpty()
            recentlyViewedReposAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@StringRes
fun UserAndRepoValidationStatus.toStringResourceMessage(): Int {
    return when (this) {
        UserAndRepoValidationStatus.EMPTY -> R.string.user_repo_validation_empty
        UserAndRepoValidationStatus.NO_SLASH_PRESENT -> R.string.user_repo_validation_no_slash_present
        UserAndRepoValidationStatus.ONE_SLASH_ONLY_PERMITTED -> R.string.user_repo_validation_one_slash_only_permitted
        UserAndRepoValidationStatus.NO_USER_NAME_FOUND -> R.string.user_repo_validation_no_user_name_found
        UserAndRepoValidationStatus.NO_REPO_NAME_FOUND -> R.string.user_repo_validation_no_repo_name_found
        UserAndRepoValidationStatus.CORRECT -> R.string.user_repo_validation_correct
    }
}

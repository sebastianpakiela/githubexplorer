package com.sebastianpakiela.githubexplorer.feature.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sebastianpakiela.githubexplorer.base.BaseFragment
import com.sebastianpakiela.githubexplorer.databinding.FragmentDetailsBinding
import com.sebastianpakiela.githubexplorer.di.ViewModelFactory
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: DetailsViewModel

    private lateinit var adapter: CommitAdapter

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.commitRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(requireActivity(), layoutManager.orientation))
            this.layoutManager = layoutManager
            adapter = CommitAdapter { viewModel.onShareClick(it) }.also {
                this@DetailsFragment.adapter = it
            }
        }

        viewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
        observeViewModel()

        args.commitList?.let {
            viewModel.initFrom(repoCommitList = it)
        }
    }

    private fun observeViewModel() {
        viewModel.commitList.observe { adapter.submitList(it) }
        viewModel.shareCommitEvent.observe {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, it)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clear()
        _binding = null
    }
}
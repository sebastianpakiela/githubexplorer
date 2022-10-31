package com.sebastianpakiela.githubexplorer.feature.search.recentlyviewed

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sebastianpakiela.githubexplorer.databinding.ItemRecentlyViewedRepositoryBinding

class RecentlyViewedReposAdapter(
    private val onItemClickListener: (String) -> Unit = {}
) : ListAdapter<String, RecentlyViewedRepoViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecentlyViewedRepoViewHolder {
        val binding = ItemRecentlyViewedRepositoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecentlyViewedRepoViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: RecentlyViewedRepoViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class RecentlyViewedRepoViewHolder(
    private val binding: ItemRecentlyViewedRepositoryBinding,
    private val onItemClickListener: (String) -> Unit
) : ViewHolder(binding.root) {

    private lateinit var repositoryKey: String

    init {
        Log.e("ADAP", "INIT1")
        binding.repository.setOnClickListener {
            onItemClickListener(repositoryKey)
            Log.e("ADAP", "called1")
        }
    }

    fun onBind(repository: String) {
        this.repositoryKey = repository
        binding.repository.text = repository
    }
}
package com.sebastianpakiela.githubexplorer.feature.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sebastianpakiela.githubexplorer.R
import com.sebastianpakiela.githubexplorer.databinding.ItemCommitBinding
import com.sebastianpakiela.githubexplorer.domain.entity.Commit

class CommitAdapter(
    private val onShareClick: (Commit) -> Unit
) : ListAdapter<Commit, CommitViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Commit>() {
            override fun areItemsTheSame(oldItem: Commit, newItem: Commit): Boolean {
                return oldItem.sha == newItem.sha
            }

            override fun areContentsTheSame(oldItem: Commit, newItem: Commit): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
        val binding = ItemCommitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommitViewHolder(binding, onShareClick)
    }

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class CommitViewHolder(private val binding: ItemCommitBinding, onShareClick: (Commit) -> Unit) :
    ViewHolder(binding.root) {

    private lateinit var commit: Commit

    init {
        binding.share.setOnClickListener { onShareClick(commit) }
    }

    fun onBind(commit: Commit) {
        this.commit = commit
        with(binding) {
            commitHash.text = commit.sha
            message.text = commit.message
            authorAndDate.text = itemView.context.getString(
                R.string.item_author_and_date,
                commit.author,
                commit.date
            )
        }
    }
}
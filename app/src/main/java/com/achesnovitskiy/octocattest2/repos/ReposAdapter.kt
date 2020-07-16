package com.achesnovitskiy.octocattest2.repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.data.Repo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposAdapter(private val onItemClickListener: (Repo) -> Unit) :
    ListAdapter<Repo, RepoViewHolder>(
        ReposDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder =
        RepoViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_repo,
                    parent,
                    false
                )
        )

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }
}

class ReposDiffCallback : DiffUtil.ItemCallback<Repo>() {

    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
}

class RepoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    private val repoTextView: TextView = containerView.repo_text_view

    fun bind(repo: Repo, onItemClickListener: (Repo) -> Unit) {

        repoTextView.text = repo.name

        itemView.setOnClickListener { onItemClickListener(repo) }
    }
}
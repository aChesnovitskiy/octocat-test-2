package com.achesnovitskiy.octocattest2.ui.repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.data.Repo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_repo.*

class ReposAdapter(val listener: (Repo) -> Unit) :
    RecyclerView.Adapter<ReposAdapter.UserViewHolder>() {
    private var repos = listOf<Repo>()

    fun updateRepos(data: List<Repo>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                repos[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                repos[oldPos] == data[newPos]

            override fun getOldListSize() = repos.size
            override fun getNewListSize() = data.size
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        repos = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_repo, parent, false
            )
        )

    override fun getItemCount(): Int = repos.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(repos[position], listener)

    inner class UserViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(repo: Repo, listener: (Repo) -> Unit) {
            repo_text_view.text = repo.name

            itemView.setOnClickListener {
                listener.invoke(repo)
            }
        }
    }
}
package com.github.binpower93.hilttest.ui.posts

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.github.binpower93.hilttest.databinding.PostListItemBinding
import com.github.binpower93.hilttest.model.Post

class PostsAdapter: RecyclerView.Adapter<PostsAdapter.BaseHolder>() {
    private val diffUtil = AsyncListDiffer(this, DiffCallback)
    private inline val data: List<Post> get() = diffUtil.currentList

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return PostHolder(
            PostListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemId(position: Int) = data[position].id.hashCode().toLong()

    fun update(newData: List<Post>) {
        Log.d("PostsVM", "$newData")
        diffUtil.submitList(newData)
    }

    abstract class BaseHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: Post)
    }

    class PostHolder(private val binding: PostListItemBinding): BaseHolder(binding) {
        override fun bind(item: Post) {
            Log.d("Bind", "$item")
            binding.post = item
        }
    }

    object DiffCallback: DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }
}
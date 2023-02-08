package dallaz.winline.common.adapter

import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class CommonViewHolder<M, B : ViewBinding>(
    protected val binding: B
) : RecyclerView.ViewHolder(binding.root) {

    open fun bindNullable(model: M?, onClick: (M) -> Unit) {
        if (model == null) binding.root.isInvisible = true
        else {
            binding.root.isInvisible = false
            bind(model, onClick)
        }
    }

    open fun bind(model: M, onClick: (M) -> Unit) {
        binding.root.setOnClickListener { onClick(model) }
        bindItem(model)
    }

    open fun bind(model: M) {
        bindItem(model)
    }

    abstract fun bindItem(model: M)
}
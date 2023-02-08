package dallaz.winline.common.adapter

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class CommonPagingAdapter<M: Any, VM: CommonViewHolder<M, *>>(
    diffCallback: DiffUtil.ItemCallback<M>,
    private val onItemClick: (M) -> Unit = {  }
) : PagingDataAdapter<M, VM>(diffCallback) {
    override fun onBindViewHolder(holder: VM, position: Int) {
        holder.bindNullable(getItem(position), onItemClick)
    }
}
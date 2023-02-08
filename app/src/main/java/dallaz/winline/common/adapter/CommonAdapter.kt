package dallaz.winline.common.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class CommonAdapter<M, VH : CommonViewHolder<M, *>>(
    private val diffCallback: DiffUtil.ItemCallback<M>,
    private val onItemClick: (M) -> Unit = { },
    initialItems: List<M> = listOf(),
) : RecyclerView.Adapter<VH>() {
    var items = initialItems
        set(value) {
            val result = calculateDiff(value)
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount() = items.size

    fun addItems(newItems: List<M>) {
        items = items.toMutableList().apply {
            addAll(newItems)
        }
    }

    protected fun calculateDiff(newItems: List<M>): DiffUtil.DiffResult =
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                diffCallback.areItemsTheSame(items[oldItemPosition], newItems[newItemPosition])


            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                diffCallback.areContentsTheSame(items[oldItemPosition], newItems[newItemPosition])
        }, true)
}
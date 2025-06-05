package otus.gpb.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import otus.gpb.recyclerview.databinding.ChatItemBinding

class ChatAdapter: ListAdapter<ChatItem, ChatItemViewHolder>(ChatItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val chatItem = getItem(position)
        val binding = holder.binding
        binding.textViewAuthor.setText(chatItem.author)
        binding.textViewTittle.setText(chatItem.title)
        binding.textViewMessage.setText(chatItem.messageLast)
        binding.textViewTime.setText(chatItem.date)
        when(chatItem.checkMark){
            CheckMark.SENT_CHECKED.name -> {
                binding.imageViewCheck.visibility = View.GONE
                binding.imageViewTwoCheck.visibility = View.VISIBLE
            }
            CheckMark.SENT_UNCHECKED.name -> {
                binding.imageViewCheck.visibility = View.VISIBLE
                binding.imageViewTwoCheck.visibility = View.GONE
            }
            else -> {
                binding.imageViewCheck.visibility = View.GONE
                binding.imageViewTwoCheck.visibility = View.GONE
            }
        }
        if (chatItem.isMentioned){
            binding.imageViewAt.visibility = View.VISIBLE
        } else {
            binding.imageViewAt.visibility = View.GONE
        }
        if (chatItem.isPinned){
            binding.imageViewPin.visibility = View.VISIBLE
        } else {
            binding.imageViewPin.visibility = View.GONE
        }
        if (chatItem.soundIsOn){
            binding.imageViewVolume.visibility = View.GONE
        } else {
            binding.imageViewVolume.visibility = View.VISIBLE
        }
        binding.imageViewAvatar.setImageResource(chatItem.image)
    }

    fun submitAntSortList(list: List<ChatItem>){
        val sortedList = list.sortedByDescending {
            it.id
            it.isPinned
        }
        submitList(sortedList)
    }
}
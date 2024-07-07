package com.example.aplikacjanotatki.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikacjanotatki.databinding.NoteLayoutBinding
import com.example.aplikacjanotatki.fragments.HomeFragmentDirections
import com.example.aplikacjanotatki.model.Note

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    // Wewnętrzna klasa NoteViewHolder, która przechowuje powiązanie dla układu notatki
    class NoteViewHolder(val itemBinding: NoteLayoutBinding): RecyclerView.ViewHolder(itemBinding.root)
    // Definicja DiffUtil.ItemCallback do efektywnego zarządzania aktualizacjami listy
    private val differCallback = object : DiffUtil.ItemCallback<Note>(){
        // Sprawdzanie, czy elementy są takie same na podstawie ich ID i zawartości
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.noteDescription == newItem.noteDescription &&
                    oldItem.noteTitle == newItem.noteTitle
        }
        // Sprawdzanie, czy zawartość elementów jest taka sama
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    // Zwraca rozmiar aktualnej listy
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    // Wiąże dane z ViewHolder na określonej pozycji
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.itemBinding.noteTitle.text = currentNote.noteTitle
        holder.itemBinding.noteDesc.text = currentNote.noteDescription
        holder.itemView.setOnClickListener{
            val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
    }
}
package com.example.aplikacjanotatki.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.aplikacjanotatki.MainActivity
import com.example.aplikacjanotatki.R
import com.example.aplikacjanotatki.databinding.FragmentEditNoteBinding
import com.example.aplikacjanotatki.model.Note
import com.example.aplikacjanotatki.viewmodel.NoteViewModel

class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {
    // Deklaracja zmiennej przechowującej powiązanie z widokiem (binding) fragmentu
    private var editNoteBinding: FragmentEditNoteBinding? = null
    // Zmienna pomocnicza zapewniająca nie-nullowalny dostęp do bindingu
    private val binding get() = editNoteBinding!!
    // Deklaracja ViewModel do zarządzania notatkami
    private lateinit var notesViewModel: NoteViewModel
    // Deklaracja zmiennej przechowującej obecnie edytowaną notatkę
    private lateinit var currentNote: Note
    // Pobranie argumentów przekazanych do fragmentu za pomocą navArgs
    private val args: EditNoteFragmentArgs by navArgs()
    // Metoda tworząca i inflatująca widok fragmentu
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicjalizacja bindingu z layoutem fragmentu
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        // Zwrócenie korzenia inflatowanego widoku
        return binding.root
    }
    // Metoda wywoływana po utworzeniu widoku fragmentu
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Konfiguracja hosta menu i dodanie MenuProvider do cyklu życia widoku
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        // Inicjalizacja notesViewModel poprzez pobranie z MainActivity
        notesViewModel = (activity as MainActivity).noteViewModel
        // Pobranie obecnej notatki z argumentów fragmentu
        currentNote = args.note!!
        // Ustawienie tytułu i opisu notatki w polach tekstowych
        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDescription)
        // Ustawienie kliknięcia na FloatingActionButton do zapisania edytowanej notatki
        binding.editNoteFab.setOnClickListener {
            // Pobranie tytułu i opisu notatki z pól tekstowych i przycięcie białych znaków
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()
            // Sprawdzenie, czy tytuł nie jest pusty
            if (noteTitle.isNotEmpty()){
                // Utworzenie zaktualizowanej notatki i aktualizacja za pomocą ViewModel
                val note = Note(currentNote.id, noteTitle, noteDesc)
                notesViewModel.updateNote(note)
                // Powrót do poprzedniego fragmentu
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                // Wyświetlenie komunikatu o konieczności podania tytułu
                Toast.makeText(context, "Proszę wprowadzić tytuł notatki.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Prywatna metoda usuwająca notatkę
    private fun deleteNote(){
        // Wyświetlenie dialogu potwierdzającego usunięcie notatki
        AlertDialog.Builder(activity).apply {
            setTitle("Usuń")
            setMessage("Czy chcesz usunąć tę notatkę?")
            setPositiveButton("Usuń"){_,_ ->
                // Usunięcie notatki za pomocą ViewModel
                notesViewModel.deleteNote(currentNote)
                // Wyświetlenie komunikatu o pomyślnym usunięciu notatki
                Toast.makeText(context, " Notatka została pomyślnie usunięta.", Toast.LENGTH_SHORT).show()
                // Powrót do poprzedniego fragmentu
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Anuluj", null)
        }.create().show()
    }
    // Metoda tworząca menu fragmentu
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Wyczyścić obecne menu i załadować nowe z pliku menu_edit_note
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }
    // Metoda obsługująca wybór elementu menu
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            // Jeśli wybrano element o ID deleteMenu, usuń notatkę i zwróć true
            R.id.deleteMenu -> {
                deleteNote()
                true
            }
            // W przeciwnym razie zwróć false
            else -> false
        }
    }
    // Metoda wywoływana przy niszczeniu fragmentu
    override fun onDestroy() {
        super.onDestroy()
        // Ustawienie bindingu na null w celu zwolnienia zasobów
        editNoteBinding = null
    }
}
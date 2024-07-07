package com.example.aplikacjanotatki.fragments

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
import com.example.aplikacjanotatki.MainActivity
import com.example.aplikacjanotatki.R
import com.example.aplikacjanotatki.databinding.FragmentAddNoteBinding
import com.example.aplikacjanotatki.model.Note
import com.example.aplikacjanotatki.viewmodel.NoteViewModel


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {
    // Deklaracja zmiennej przechowującej powiązanie z widokiem (binding) fragmentu
    private var addNoteBinding: FragmentAddNoteBinding? = null
    // Zmienna pomocnicza zapewniająca nie-nullowalny dostęp do bindingu
    private val binding get() = addNoteBinding!!
    // Deklaracja ViewModel do zarządzania notatkami
    private lateinit var notesViewModel: NoteViewModel
    // Deklaracja zmiennej przechowującej widok fragmentu
    private lateinit var addNoteView: View

    // Metoda tworząca i inflatująca widok fragmentu
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicjalizacja bindingu z layoutem fragmentu
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
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
        // Ustawienie zmiennej addNoteView na obecny widok
        addNoteView = view
    }
    // Prywatna metoda zapisująca notatkę
    private fun saveNote(view: View){
        // Pobranie tytułu i opisu notatki z pól tekstowych i przycięcie białych znaków
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()
        // Sprawdzenie, czy tytuł nie jest pusty
        if (noteTitle.isNotEmpty()){
            // Utworzenie nowej notatki i dodanie jej za pomocą ViewModel
            val note = Note(0, noteTitle, noteDesc)
            notesViewModel.addNote(note)

            // Wyświetlenie komunikatu o zapisaniu notatki
            Toast.makeText(addNoteView.context, "Zapisano!", Toast.LENGTH_SHORT).show()
            // Powrót do poprzedniego fragmentu
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            // Wyświetlenie komunikatu o konieczności podania tytułu
            Toast.makeText(addNoteView.context, "Tytuł jest wymagany. Spróbuj ponownie.", Toast.LENGTH_SHORT).show()
        }
    }
    // Metoda tworząca menu fragmentu
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Wyczyścić obecne menu i załadować nowe z pliku menu_add_note
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }
    // Metoda obsługująca wybór elementu menu
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            // Jeśli wybrano element o ID saveMenu, zapisz notatkę i zwróć true
            R.id.saveMenu -> {
                saveNote(addNoteView)
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
        addNoteBinding = null
    }
}
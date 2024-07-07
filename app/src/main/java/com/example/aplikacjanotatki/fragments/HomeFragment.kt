package com.example.aplikacjanotatki.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikacjanotatki.MainActivity
import com.example.aplikacjanotatki.R
import com.example.aplikacjanotatki.adapter.NoteAdapter
import com.example.aplikacjanotatki.databinding.FragmentHomeBinding
import com.example.aplikacjanotatki.model.Note
import com.example.aplikacjanotatki.viewmodel.NoteViewModel

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {
    // Deklaracja zmiennej homeBinding do wiązania widoku fragmentu
    private var homeBinding: FragmentHomeBinding? = null
    // Get dla homeBinding, aby uniknąć nulli
    private val binding get() = homeBinding!!
    // Deklaracja później inicjalizowanych zmiennych notesViewModel i noteAdapter
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    // Metoda onCreateView jest wywoływana podczas tworzenia widoku fragmentu
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicjalizacja homeBinding przy użyciu inflatora
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        // Zwrócenie korzenia widoku
        return binding.root
    }
    // Metoda onViewCreated jest wywoływana po stworzeniu widoku fragmentu
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Dodanie menu do fragmentu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        // Inicjalizacja notesViewModel z aktywności rodzica
        notesViewModel = (activity as MainActivity).noteViewModel
        // Konfiguracja RecyclerView
        setupHomeRecyclerView()
        // Ustawienie akcji na kliknięcie przycisku "Dodaj notatkę"
        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }
    // Metoda do aktualizacji UI na podstawie listy notatek
    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if (note.isNotEmpty()) {
                // Pokaż RecyclerView, jeśli lista notatek nie jest pusta
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                // Ukryj RecyclerView, jeśli lista notatek jest pusta
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }
    // Metoda konfigurująca RecyclerView
    private fun setupHomeRecyclerView() {
        // Inicjalizacja adaptera notatek
        noteAdapter = NoteAdapter()
        // Konfiguracja RecyclerView
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = noteAdapter
        }
        // Obserwacja zmian w liście notatek i aktualizacja adaptera
        activity?.let {
            notesViewModel.getAllNotes().observe(viewLifecycleOwner) { note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }
    // Metoda wyszukująca notatki na podstawie zapytania
    private fun searchNote(query: String?) {
        val searchQuery = "%$query"
        // Obserwacja wyników wyszukiwania i aktualizacja adaptera
        notesViewModel.searchNote(searchQuery).observe(this) { list ->
            noteAdapter.differ.submitList(list)
        }
    }
    // Metoda wywoływana po wysłaniu zapytania w SearchView
    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }
    // Metoda wywoływana po zmianie tekstu w SearchView
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }
    // Metoda wywoływana przy niszczeniu widoku fragmentu
    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
    }
    // Metoda tworząca menu
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)
        // Konfiguracja SearchView w menu
        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }
    // Metoda obsługująca wybór elementu menu
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}

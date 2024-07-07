package com.example.aplikacjanotatki

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.aplikacjanotatki.database.NoteDatabase
import com.example.aplikacjanotatki.repository.NoteRepository
import com.example.aplikacjanotatki.viewmodel.NoteViewModel
import com.example.aplikacjanotatki.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var noteViewModel: NoteViewModel
    // Metoda onCreate jest wywoływana podczas tworzenia aktywności
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Włączenie trybu Edge to Edge
        enableEdgeToEdge()
        // Ustawienie widoku aktywności na layout activity_main
        setContentView(R.layout.activity_main)
        // Konfiguracja ViewModel
        setupViewModel()
    }
    // Prywatna metoda konfigurująca ViewModel
    private fun setupViewModel(){
        // Utworzenie instancji NoteRepository z bazą danych NoteDatabase
        val noteRepository = NoteRepository(NoteDatabase(this))
        // Utworzenie fabryki ViewModel z aplikacją i repozytorium notatek
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        // Inicjalizacja noteViewModel przy użyciu ViewModelProvider i fabryki
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }
}

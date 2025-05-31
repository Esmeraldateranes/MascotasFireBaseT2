package com.example.mascotasfirebaset2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mascotasfirebaset2.adapter.PetAdapter
import com.example.mascotasfirebaset2.model.Pet
import com.example.mascotasfirebaset2.viewmodel.PetViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: PetViewModel
    private lateinit var adapter: PetAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        toolbar.setBackgroundColor(resources.getColor(android.R.color.white))
        toolbar.setTitleTextColor(resources.getColor(android.R.color.black))

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            Log.d("MainActivity", "No authenticated user")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        } else {
            Log.d("MainActivity", "Authenticated user: ${auth.currentUser?.uid}")
        }

        viewModel = ViewModelProvider(this).get(PetViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.petRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        emptyTextView = findViewById(R.id.emptyTextView)

        viewModel.getPets().observe(this) { pets ->
            Log.d("MainActivity", "Pets received: ${pets?.size ?: 0}")
            adapter = PetAdapter(pets ?: emptyList(), object : PetAdapter.OnPetClickListener {
                override fun onEditClick(pet: Pet) {
                    val intent = Intent(this@MainActivity, PetFormActivity::class.java).apply {
                        putExtra("PET", pet)
                    }
                    startActivity(intent)
                }

                override fun onDeleteClick(pet: Pet) {
                    viewModel.deletePet(pet.id)
                }
            })
            recyclerView.adapter = adapter
            emptyTextView.visibility = if (pets.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        val addPetButton = findViewById<Button>(R.id.addPetButton)
        addPetButton.setOnClickListener {
            startActivity(Intent(this, PetFormActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
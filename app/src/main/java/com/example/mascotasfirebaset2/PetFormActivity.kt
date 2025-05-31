package com.example.mascotasfirebaset2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mascotasfirebaset2.model.Pet
import com.example.mascotasfirebaset2.repository.PetRepository
import com.example.mascotasfirebaset2.viewmodel.PetViewModel

class PetFormActivity : AppCompatActivity() {
    private lateinit var viewModel: PetViewModel
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var imageUrlEditText: EditText
    private lateinit var petImageView: ImageView
    private var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_form)

        // Configurar Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        pet = intent.getSerializableExtra("PET") as? Pet
        supportActionBar?.title = if (pet == null) "Nueva Mascota" else "Editar Mascota"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(PetViewModel::class.java)
        nameEditText = findViewById(R.id.nameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        ageEditText = findViewById(R.id.ageEditText)
        imageUrlEditText = findViewById(R.id.imageUrlEditText)
        petImageView = findViewById(R.id.petImageView)
        val saveButton = findViewById<Button>(R.id.saveButton)

        pet?.let {
            nameEditText.setText(it.name)
            descriptionEditText.setText(it.description)
            ageEditText.setText(it.age.toString())
            imageUrlEditText.setText(it.imageUrl)
            if (!it.imageUrl.isNullOrEmpty()) {
                Glide.with(this).load(it.imageUrl).into(petImageView)
            }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val ageStr = ageEditText.text.toString().trim()
            val imageUrl = imageUrlEditText.text.toString().trim()

            if (name.isNotEmpty() && description.isNotEmpty() && ageStr.isNotEmpty()) {
                try {
                    val age = ageStr.toInt()
                    if (pet == null) {
                        pet = Pet("", name, description, age, imageUrl)
                        viewModel.addPet(pet!!, object : PetRepository.Callback {
                            override fun onSuccess() {
                                Log.d("PetFormActivity", "Pet added successfully")
                                Toast.makeText(this@PetFormActivity, "Mascota guardada", Toast.LENGTH_SHORT).show()
                                finish()
                            }

                            override fun onError(e: Exception) {
                                Log.e("PetFormActivity", "Error adding pet", e)
                                Toast.makeText(this@PetFormActivity, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        pet?.apply {
                            this.name = name
                            this.description = description
                            this.age = age
                            this.imageUrl = imageUrl
                        }
                        viewModel.updatePet(pet!!, object : PetRepository.Callback {
                            override fun onSuccess() {
                                Log.d("PetFormActivity", "Pet updated successfully")
                                Toast.makeText(this@PetFormActivity, "Mascota actualizada", Toast.LENGTH_SHORT).show()
                                finish()
                            }

                            override fun onError(e: Exception) {
                                Log.e("PetFormActivity", "Error updating pet", e)
                                Toast.makeText(this@PetFormActivity, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
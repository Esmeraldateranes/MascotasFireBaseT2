package com.example.mascotasfirebaset2.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mascotasfirebaset2.model.Pet
import com.example.mascotasfirebaset2.repository.PetRepository

class PetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PetRepository()

    fun getPets(): LiveData<List<Pet>> = repository.getPets()

    fun addPet(pet: Pet, callback: PetRepository.Callback) {
        repository.addPet(pet, object : PetRepository.Callback {
            override fun onSuccess() {
                Toast.makeText(getApplication(), "Mascota guardada", Toast.LENGTH_SHORT).show()
                callback.onSuccess()
            }

            override fun onError(e: Exception) {
                Toast.makeText(getApplication(), "Error al guardar", Toast.LENGTH_SHORT).show()
                callback.onError(e)
            }
        })
    }

    fun updatePet(pet: Pet, callback: PetRepository.Callback) {
        repository.updatePet(pet, object : PetRepository.Callback {
            override fun onSuccess() {
                Toast.makeText(getApplication(), "Mascota actualizada", Toast.LENGTH_SHORT).show()
                callback.onSuccess()
            }

            override fun onError(e: Exception) {
                Toast.makeText(getApplication(), "Error al actualizar", Toast.LENGTH_SHORT).show()
                callback.onError(e)
            }
        })
    }

    fun deletePet(petId: String) {
        repository.deletePet(petId, object : PetRepository.Callback {
            override fun onSuccess() {
                Toast.makeText(getApplication(), "Mascota eliminada", Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Exception) {
                Toast.makeText(getApplication(), "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
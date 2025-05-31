package com.example.mascotasfirebaset2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mascotasfirebaset2.model.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PetRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val petsLiveData = MutableLiveData<List<Pet>>()

    fun getPets(): LiveData<List<Pet>> {
        if (auth.currentUser == null) {
            Log.d("PetRepository", "No authenticated user")
            petsLiveData.value = emptyList()
            return petsLiveData
        }

        val userId = auth.currentUser!!.uid
        db.collection("users").document(userId).collection("pets")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("PetRepository", "Error fetching pets", error)
                    petsLiveData.value = emptyList()
                    return@addSnapshotListener
                }
                val pets = mutableListOf<Pet>()
                Log.d("PetRepository", "Documents received: ${value?.size()}")
                value?.documents?.forEach { doc ->
                    val pet = doc.toObject(Pet::class.java)?.apply { id = doc.id }
                    pet?.let { pets.add(it) }
                    Log.d("PetRepository", "Pet: ${pet?.name}")
                }
                petsLiveData.value = pets
            }
        return petsLiveData
    }

    fun addPet(pet: Pet, callback: Callback) {
        if (auth.currentUser == null) {
            Log.e("PetRepository", "Usuario no autenticado")
            callback.onError(Exception("Usuario no autenticado"))
            return
        }

        val userId = auth.currentUser!!.uid
        val petsRef = db.collection("users").document(userId).collection("pets")
        petsRef.add(pet)
            .addOnSuccessListener { documentReference ->
                Log.d("PetRepository", "Pet added with ID: ${documentReference.id}")
                callback.onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("PetRepository", "Error adding pet", e)
                callback.onError(e)
            }
    }

    fun updatePet(pet: Pet, callback: Callback) {
        if (auth.currentUser == null) {
            Log.e("PetRepository", "Usuario no autenticado")
            callback.onError(Exception("Usuario no autenticado"))
            return
        }

        val userId = auth.currentUser!!.uid
        db.collection("users").document(userId).collection("pets").document(pet.id)
            .set(pet)
            .addOnSuccessListener {
                Log.d("PetRepository", "Pet updated: ${pet.id}")
                callback.onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("PetRepository", "Error updating pet", e)
                callback.onError(e)
            }
    }

    fun deletePet(petId: String, callback: Callback) {
        if (auth.currentUser == null) {
            Log.e("PetRepository", "Usuario no autenticado")
            callback.onError(Exception("Usuario no autenticado"))
            return
        }

        val userId = auth.currentUser!!.uid
        db.collection("users").document(userId).collection("pets").document(petId)
            .delete()
            .addOnSuccessListener {
                Log.d("PetRepository", "Pet deleted: $petId")
                callback.onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("PetRepository", "Error deleting pet", e)
                callback.onError(e)
            }
    }

    interface Callback {
        fun onSuccess()
        fun onError(e: Exception)
    }
}
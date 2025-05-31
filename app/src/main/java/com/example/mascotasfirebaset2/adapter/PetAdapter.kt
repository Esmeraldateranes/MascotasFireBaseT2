package com.example.mascotasfirebaset2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mascotasfirebaset2.R
import com.example.mascotasfirebaset2.model.Pet

class PetAdapter(
    private val pets: List<Pet> = emptyList(),
    private val listener: OnPetClickListener
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    interface OnPetClickListener {
        fun onEditClick(pet: Pet)
        fun onDeleteClick(pet: Pet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        Log.d("PetAdapter", "Binding pet: ${pet.name}")
        holder.nameTextView.text = pet.name
        holder.descriptionTextView.text = pet.description
        holder.ageTextView.text = pet.age.toString()
        if (!pet.imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(pet.imageUrl)
                .into(holder.petImageView)
        }
        holder.editButton.setOnClickListener { listener.onEditClick(pet) }
        holder.deleteButton.setOnClickListener { listener.onDeleteClick(pet) }
    }

    override fun getItemCount(): Int = pets.size

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petImageView: ImageView = itemView.findViewById(R.id.petImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val ageTextView: TextView = itemView.findViewById(R.id.ageTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }
}
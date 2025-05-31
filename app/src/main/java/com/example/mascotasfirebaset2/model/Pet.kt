package com.example.mascotasfirebaset2.model

import java.io.Serializable

data class Pet(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var age: Int = 0,
    var imageUrl: String? = null
) : Serializable
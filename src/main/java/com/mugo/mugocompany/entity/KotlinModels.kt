package com.mugo.mugocompany

import java.util.*

class GenerateObject : ArrayList<GenerateObjectItem>()

data class GenerateObjectItem(
    val clientName: String,
    val createdAt: String,
    val id: String,
    val registrationNumber: String,
    val risk: String,
    val status: String,
    val updatedAt: String
)
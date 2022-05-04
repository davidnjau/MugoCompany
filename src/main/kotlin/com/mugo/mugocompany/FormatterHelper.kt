package com.mugo.mugocompany

import com.mugo.mugocompany.entity.ClientDetails
import com.mugo.mugocompany.repository.ClientDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FormatterHelper {

    fun saveClientDataList(
        clientDetails: MutableList<ClientDetails>,
        clientDetailsRepository: ClientDetailsRepository
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            createClient(clientDetails, clientDetailsRepository)
        }
    }

    private suspend fun createClient(clientDetails: MutableList<ClientDetails>, clientDetailsRepository: ClientDetailsRepository) {
        coroutineScope {
            launch(Dispatchers.IO) {
                clientDetailsRepository.saveAll(clientDetails)
            }
        }
    }
}
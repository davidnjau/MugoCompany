package com.mugo.mugocompany

import com.mugo.mugocompany.entity.ClientDetails
import com.mugo.mugocompany.entity.SanlamData
import com.mugo.mugocompany.repository.ClientDetailsRepository
import com.mugo.mugocompany.repository.SanlamDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    fun saveSanlamDataList(
        sanlamDataList: MutableList<SanlamData>,
        sanlamDataRepository: SanlamDataRepository
    ){
        CoroutineScope(Dispatchers.IO).launch {
            createSanlam(sanlamDataList, sanlamDataRepository)
        }
    }

    private suspend fun createSanlam(sanlamDataList: MutableList<SanlamData>, sanlamDataRepository: SanlamDataRepository) {

        coroutineScope {
            launch(Dispatchers.IO) {
                sanlamDataRepository.saveAll(sanlamDataList)
            }
        }

    }


    fun getSanlamDataName(): String {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHH:mm")
        val formatted = current.format(formatter)

        return "SAN/$formatted"

    }
}
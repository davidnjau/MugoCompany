package com.mugo.mugocompany

import com.mugo.mugocompany.entity.ClientDetails
import com.mugo.mugocompany.entity.SanlamData
import com.mugo.mugocompany.entity.SanlamList
import com.mugo.mugocompany.repository.ClientDetailsRepository
import com.mugo.mugocompany.repository.SanlamDataRepository
import com.mugo.mugocompany.repository.SanlamListRepository
import com.mugo.mugocompany.servicemanager.impl.ClientDetailsServiceImpl
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


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
        sanlamDataList: MutableList<DbSanlamData>,
        sanlamDataRepository: SanlamDataRepository,
        sanlamList: SanlamList,
        sanlamListRepository: SanlamListRepository
    ){
        CoroutineScope(Dispatchers.IO).launch {
            createSanlam(sanlamDataList,sanlamDataRepository,sanlamList,sanlamListRepository)
        }
    }

    private suspend fun createSanlam(
        dbSanlamDataList: MutableList<DbSanlamData>,
        sanlamDataRepository: SanlamDataRepository,
        sanlamList: SanlamList,
        sanlamListRepository: SanlamListRepository
    ) {

        var returnSanlamList: SanlamList? = null
        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {

            returnSanlamList = sanlamListRepository.save(sanlamList)

        }.join()

        coroutineScope {
            launch(Dispatchers.IO) {

                var totalAmount = 0

                val sanlamDataList: MutableList<SanlamData> = ArrayList()

                for (i in dbSanlamDataList.indices) {

                    val claimNumber: String = dbSanlamDataList[i].claimNumber
                    val amount: String = dbSanlamDataList[i].amount
                    val narration: String = dbSanlamDataList[i].narration
                    totalAmount += amount.toInt()

                    val regNo = getRegistrationNumber(narration)


                    val sanlamData = SanlamData(claimNumber, amount, narration, regNo, returnSanlamList?.id)
                    sanlamDataList.add(sanlamData)
                }

                val newTotal = currencyFormatter(totalAmount)

                //Update Total Amount
                if (returnSanlamList != null){
                    sanlamListRepository.findById(sanlamList.id)
                        .map { sanlamListOld ->
                            sanlamListOld.totalAmount = newTotal
                            sanlamListRepository.save(sanlamListOld)
                        }.orElse(null)

                }

                sanlamDataRepository.saveAll(sanlamDataList)
            }
        }

    }

    fun getRegistrationNumber(narration: String): String {

        val reversedNarration = reverse(narration)
        val newReversedNarration = reversedNarration.substring(0, 9)
        return reverse(newReversedNarration)

    }
    private fun reverse(sentence: String): String {
        if (sentence.isEmpty())
            return sentence
        return reverse(sentence.substring(1)) + sentence[0]
    }


    fun getSanlamDataName(): String {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
        val formatted = current.format(formatter)

        return "SAN/$formatted"

    }

    fun currencyFormatter(num: Int): String {

        val amount = num.toDouble()
        val formatter = DecimalFormat("#,###.00")
        val newTotal = formatter.format(amount)

        return newTotal
    }

    fun dateConverter(dateString: String): String {
        val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = originalFormat.parse(dateString)
        return targetFormat.format(date)
    }

    fun extractBacValues(
        sanlamId: String,
        sanlamDataRepository: SanlamDataRepository,
        clientDetailsService: ClientDetailsServiceImpl
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            extractValueData(sanlamId, sanlamDataRepository, clientDetailsService)
        }
    }
    private suspend fun extractValueData(
        sanlamId: String,
        sanlamDataRepository: SanlamDataRepository,
        clientDetailsService: ClientDetailsServiceImpl
    ) {

        coroutineScope {
            launch(Dispatchers.IO) {

                val sanlamDataList = sanlamDataRepository.findBySanlamListId(sanlamId)
                for (item in sanlamDataList){

                    val claimNumber = item.claimNumber
                    val amount = item.amount
                    val narration = item.narration
                    val registrationNumber = item.registrationNumber

                    //get owner of reg. number
                    val clientData = clientDetailsService.getClientByRegNo(registrationNumber)
                    if (clientData != null){

                        println("--------")
                        println(clientData.clientName)
                        println(registrationNumber)
                        println(amount)

                    }

                }

                println(sanlamDataList)

            }
        }

    }

}
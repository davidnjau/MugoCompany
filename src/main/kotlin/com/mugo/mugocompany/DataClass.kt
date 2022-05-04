package com.mugo.mugocompany

import com.fasterxml.jackson.annotation.JsonProperty

data class Results(val code: Int, val details: Any)

data class ResponseMessage(val details: Any)

data class DbActs(
    @JsonProperty("actName")
    val actName: String)

data class DbClientInfo(
    val clientName: String,
    val regNumber: String,
    val risk: String,
    val status: String,
)
data class DbSanlamData(
    val claimNumber: String,
    val amount:String,
    val narration: String,
    val regNumber: String,
)
data class DbClientRes(
    val int: Int,
    val nextPage: String?,
    val previousPage:String?,
    val results: List<DbClientInfo>
)

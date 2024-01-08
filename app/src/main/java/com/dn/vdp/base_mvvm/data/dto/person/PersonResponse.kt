package com.dn.vdp.base_mvvm.data.dto.person

data class PersonResponse (
    val results: List<Person>,
    val info: Info
)

data class Info (
    val seed: String,
    val results: Long,
    val page: Long,
    val version: String
)
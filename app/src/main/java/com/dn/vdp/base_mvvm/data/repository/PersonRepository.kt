package com.dn.vdp.base_mvvm.data.repository

import com.dn.vdp.base_mvvm.data.dto.person.Person
import com.dn.vdp.base_mvvm.data.retofit.PersonApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

interface PersonRepository {
    fun getPersonInfo(): Flow<Person>
}

@OptIn(FlowPreview::class)
class PersonRepositoryImpl @Inject constructor(
    private val personApi: PersonApi
) : PersonRepository {
    override fun getPersonInfo(): Flow<Person> {
        return suspend { personApi.getPersonInfo().results.first() }.asFlow()
    }
}
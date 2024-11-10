package com.alphaStore.Core.contracts

import com.alphaStore.Core.entity.AccessRole

interface AccessRoleDataRepoAggregatorContract{

    fun save(entity: AccessRole): AccessRole

    fun executeFunction(queryToExecute: String): List<AccessRole>
    fun saveAll(entities: List<AccessRole>)

    fun dropTable()

    fun deleteAll()
}
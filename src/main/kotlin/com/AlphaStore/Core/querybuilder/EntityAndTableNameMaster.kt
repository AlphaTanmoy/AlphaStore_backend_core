package com.alphaStore.Core.querybuilder

import com.alphaStore.Core.model.EntityAndTableName
import com.alphaStore.Core.model.PaginationResponse
import com.alphaStore.Utils.contracts.BadRequestException
import org.springframework.stereotype.Component

@Component
class EntityAndTableNameMaster {

    private val listOfEntityAndTableNameMaster: ArrayList<EntityAndTableName> = arrayListOf(
        EntityAndTableName(
            "AccessRole",
            "access_roles"
        ),

    )

    fun getFromEntityName(entityName: String): EntityAndTableName {
        listOfEntityAndTableNameMaster.find { toFind -> toFind.entity == entityName }?.let { return it } ?: run {
            throw BadRequestException("Failed to find entity")
        }
    }

    fun getFromTableName(tableName: String): EntityAndTableName {
        listOfEntityAndTableNameMaster.find { toFind -> toFind.table == tableName }?.let { return it } ?: run {
            throw BadRequestException("Failed to find entity")
        }
    }

    fun getAllTableNames(): ArrayList<String> {
        val toRet: ArrayList<String> = ArrayList()
        listOfEntityAndTableNameMaster.forEach {
            toRet.add(it.table)
        }
        return toRet
    }

    fun getAllEntityNames(): PaginationResponse<String> {
        val toRet: ArrayList<String> = ArrayList()
        listOfEntityAndTableNameMaster.forEach {
            toRet.add(it.entity)
        }
        return PaginationResponse(toRet)
    }
}
package com.alphaStore.Core.repoAggregator

import com.alphaStore.Core.contracts.AccessRoleDataRepoAggregatorContract
import com.alphaStore.Core.entity.AccessRole
import org.springframework.stereotype.Component

@Component
class AccessRoleDataRepoAggregator(
    private val accessRole: AccessRole,
) : AccessRoleDataRepoAggregatorContract {

    override fun save(entity: AccessRole): AccessRole {
        return accessRole.save(entity)
    }

    override fun executeFunction(queryToExecute: String): List<AccessRole> {
        return accessRole.executeFunction(queryToExecute)
    }

    override fun saveAll(entities: List<AccessRole>) {
        accessRole.saveAll(entities)
    }

    override fun dropTable() {
        accessRole.dropTable()
    }

    override fun deleteAll() {
        accessRole.deleteAll()
    }
}
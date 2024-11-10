package com.alphaStore.Core.repoAggregator

import com.alphaStore.Core.contracts.AccessRoleRepoAggregatorContract
import com.alphaStore.Core.entity.AccessRole
import com.alphaStore.Core.repo.AccessRoleRepo
import org.springframework.stereotype.Component

@Component
class AccessRoleRepoAggregator(
    private val accessRoleRepo: AccessRoleRepo,
) : AccessRoleRepoAggregatorContract {

    override fun save(entity: AccessRole): AccessRole {
        return accessRoleRepo.save(entity)
    }

    override fun executeFunction(queryToExecute: String): List<AccessRole> {
        return accessRoleRepo.executeFunction(queryToExecute)
    }

    override fun saveAll(entities: List<AccessRole>) {
        accessRoleRepo.saveAll(entities)
    }

    override fun dropTable() {
        accessRoleRepo.dropTable()
    }

    override fun deleteAll() {
        accessRoleRepo.deleteAll()
    }
}

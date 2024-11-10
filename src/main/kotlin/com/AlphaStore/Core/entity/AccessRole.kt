package com.alphaStore.Core.entity

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonFilter
import com.alphaStore.Core.enums.AccessRoleType

@Entity(name = "access_roles")
data class AccessRole(
    @Column(nullable = false, unique = true)
    var title: String = "",

    @Column(nullable = false)
    var description: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "access_role_type", nullable = false)
    var accessRoleType: AccessRoleType
) : SuperEntityWithIdCreatedLastModifiedDataStatus()

@JsonFilter("AccessRoleFilter")
class AccessRoleMixIn

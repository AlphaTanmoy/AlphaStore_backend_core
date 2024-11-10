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

    @Column(nullable = false)
    var code: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "access_role_type", nullable = false)
    var accessRoleType: AccessRoleType  // Holds one value from AccessRoleType enum
) : SuperEntityWithParentId()

@JsonFilter("AccessRoleFilter")
class AccessRoleMixIn

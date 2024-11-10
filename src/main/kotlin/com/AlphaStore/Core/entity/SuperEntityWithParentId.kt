package com.alphaStore.Core.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class SuperEntityWithParentId(
    @Column(nullable = false)
    var parentId: String = "",
)
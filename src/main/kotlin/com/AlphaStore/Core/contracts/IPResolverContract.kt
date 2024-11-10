package com.alphaStore.Core.contracts

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.server.ServerWebExchange

interface IPResolverContract {

    fun resolve(request: HttpServletRequest): String

    fun resolve(serverWebExchange: ServerWebExchange): String
}
package com.alphaStore.Core.model

import com.alphaStore.Core.enums.TokenType
import java.time.ZonedDateTime

data class SoftTokenResponse(
    var tokenType: TokenType = TokenType.SOFT_TOKEN_TIRE_SIX,
    var expTimeSuggest: ZonedDateTime? = null,
    var forceResetPasswordRequired: Boolean = false
)
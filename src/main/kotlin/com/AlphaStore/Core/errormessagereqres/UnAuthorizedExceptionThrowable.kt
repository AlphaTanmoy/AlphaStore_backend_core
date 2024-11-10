package com.alphaStore.Core.errormessagereqres

class UnAuthorizedExceptionThrowable(
    var errorMessage: String = "",
    var code: Int? = null
) : Throwable()
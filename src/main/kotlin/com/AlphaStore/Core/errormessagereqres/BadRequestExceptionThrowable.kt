package com.alphaStore.Core.errormessagereqres

class BadRequestExceptionThrowable(
    var errorMessage: String = ""
) : Throwable()
package com.alphaStore.Core.contracts

interface EncodingUtilContract {

    fun encode(toEncode: String): String

    fun decode(toDecode: String): String
}
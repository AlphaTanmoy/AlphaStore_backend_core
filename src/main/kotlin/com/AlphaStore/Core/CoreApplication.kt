package com.alphaStore.Core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication(
	scanBasePackages = [
		"com.alphaStore.Core",
		"com.alphaStore.Utils",
		"com.alphaStore.Admin",
		"com.alphaStore.Mail",
		"com.alphaStore.Otp"
	]
)

@EnableDiscoveryClient
class CoreApplication

fun main(args: Array<String>) {
	runApplication<CoreApplication>(*args)
}

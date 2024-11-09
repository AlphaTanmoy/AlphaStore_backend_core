package com.alphaStore.Core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = [
	"com.alphaStore.Core",
	"com.alphaStore.Utils",
	"com.alphaStore.Admin",
	"com.alphaStore.Mail",
	"com.alphaStore.Otp"
])
class CoreApplication

fun main(args: Array<String>) {
	runApplication<CoreApplication>(*args)
}

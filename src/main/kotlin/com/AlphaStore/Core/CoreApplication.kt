package com.AlphaStore.Core

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = [
    "com.AlphaStore.Core",
    "com.AlphaStore.Utils"
])
class CoreApplication

fun main(args: Array<String>) {
    SpringApplication.run(CoreApplication::class.java, *args)
}

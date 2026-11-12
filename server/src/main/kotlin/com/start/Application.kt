package com.start

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    // this should return json response
    install(ContentNegotiation) {
        json()
    }

    // add all the routes here
    routing {
        get("/") {
            call.respondText(sayHello("Ktor"))
        }

        get("/health") {
            call.respond(HealthResponse(status = "OK"))
        }
    }
}

@Serializable
data class HealthResponse(
    val status: String
)
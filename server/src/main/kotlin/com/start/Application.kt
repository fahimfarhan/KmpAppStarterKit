package com.start

import com.start.models.Todos
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

private val todos = mutableListOf(
    Todos(id = 1, name = "Learn Ktor"),
    Todos(id = 2, name = "Build Todo App"),
)

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

        get("/api/todos") {
            call.respond(todos)
        }

        post("/api/todos") {
            val todo = call.receive<Todos>()
            todos.add(todo)
            call.respond(todo)
        }

        get(path = "/api/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val todo = todos.find { ithTodo -> ithTodo.id == id }

            if(null == todo) {
                call.respond(HttpStatusCode.NotFound, "Todo not found")
                return@get
            }
            call.respond(todo)
        }


        /**
         * curl -X DELETE http://localhost:8080/api/todos/1
         * */
        delete(path = "/api/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            val removed = todos.removeIf { ithTodo -> ithTodo.id == id }

            if (removed) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Todo not found")
            }
        }
    }
}

@Serializable
data class HealthResponse(
    val status: String
)
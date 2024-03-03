package me.sebastijanzindl.websockets

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.sebastijanzindl.websockets.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
fun Application.module() {
    configureSockets()
    configureRouting()
}

package me.sebastijanzindl.websockets.plugins

import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.*
import me.sebastijanzindl.websockets.Connection

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
       webSocket("/chat") {
           println("Adding user");

           val thisConn = Connection(this);
           connections += thisConn;
           try {
               send("You are connected! There are ${connections.count()} users here.")
               for(frame in incoming) {
                   frame as? Frame.Text ?: continue;
                   val receivedText = frame.readText()

                   val textWithUsername = "[${thisConn.name}]: $receivedText"
                   connections.forEach {
                       it.session.send(textWithUsername)
                   }
               }
           } catch (e: Exception) {
               println(e.localizedMessage);
           } finally {
               println("Removing $thisConn")
               connections -= thisConn;
           }
       }
    }
}

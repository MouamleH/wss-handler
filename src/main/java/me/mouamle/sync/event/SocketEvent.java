package me.mouamle.sync.event;

import org.java_websocket.WebSocket;

import java.util.Objects;

public class SocketEvent extends Event {

    private final WebSocket socket;

    public SocketEvent(WebSocket socket) {
        this.socket = socket;
    }

    public WebSocket getSocket() {
        return socket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketEvent that = (SocketEvent) o;
        return Objects.equals(socket, that.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket);
    }

    @Override
    public String toString() {
        return "SocketEvent{" +
                "socket=" + socket +
                '}';
    }

}

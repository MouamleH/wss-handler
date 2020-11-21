package me.mouamle.sync.event;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.util.Objects;

public class OnOpen extends SocketEvent {

    private final ClientHandshake handshake;

    public OnOpen(WebSocket socket, ClientHandshake handshake) {
        super(socket);
        this.handshake = handshake;
    }

    public ClientHandshake getHandshake() {
        return handshake;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnOpen onOpen = (OnOpen) o;
        return Objects.equals(handshake, onOpen.handshake);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), handshake);
    }

    @Override
    public String toString() {
        return "OnOpen{" +
                "handshake=" + handshake +
                '}';
    }

}

package me.mouamle.sync.event;

import org.java_websocket.WebSocket;

import java.util.Objects;

public class OnMessage extends SocketEvent {

    private final String message;

    private final boolean handled;
    private final String reason;

    public OnMessage(WebSocket socket, String message, boolean handled, String reason) {
        super(socket);
        this.message = message;
        this.handled = handled;
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHandled() {
        return handled;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnMessage onMessage = (OnMessage) o;
        return handled == onMessage.handled &&
                Objects.equals(message, onMessage.message) &&
                Objects.equals(reason, onMessage.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), message, handled, reason);
    }

    @Override
    public String toString() {
        return "OnMessage{" +
                "message='" + message + '\'' +
                ", handled=" + handled +
                ", reason='" + reason + '\'' +
                '}';
    }

}

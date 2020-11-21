package me.mouamle.sync.event;

import org.java_websocket.WebSocket;

import java.util.Objects;

public class OnError extends SocketEvent {

    private final Exception exception;

    public OnError(WebSocket socket, Exception exception) {
        super(socket);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnError onError = (OnError) o;
        return Objects.equals(exception, onError.exception);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), exception);
    }

    @Override
    public String toString() {
        return "OnError{" +
                "ex=" + exception +
                '}';
    }

}

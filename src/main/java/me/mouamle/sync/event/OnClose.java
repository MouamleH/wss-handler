package me.mouamle.sync.event;

import org.java_websocket.WebSocket;

import java.util.Objects;

public class OnClose extends SocketEvent {

    private final int code;
    private final String reason;
    private final boolean remote;

    public OnClose(WebSocket socket, int code, String reason, boolean remote) {
        super(socket);
        this.code = code;
        this.reason = reason;
        this.remote = remote;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public boolean isRemote() {
        return remote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnClose onClose = (OnClose) o;
        return code == onClose.code &&
                remote == onClose.remote &&
                Objects.equals(reason, onClose.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code, reason, remote);
    }

    @Override
    public String toString() {
        return "OnClose{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                ", remote=" + remote +
                '}';
    }
}

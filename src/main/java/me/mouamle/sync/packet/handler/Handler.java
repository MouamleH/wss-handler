package me.mouamle.sync.packet.handler;

import com.google.gson.Gson;
import me.mouamle.sync.packet.Packet;
import org.java_websocket.WebSocket;

public abstract class Handler<T> {

    protected final Gson gson = new Gson();

    public abstract void handle(WebSocket socket, T payload);

    public abstract String getPacketName();

    public abstract Class<T> getPayloadType();

    protected void reply(WebSocket socket, Object response) {
        socket.send(gson.toJson(new Packet(getPacketName(), response)));
    }

}

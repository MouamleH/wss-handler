package me.mouamle.sync.packet.handler;

import com.google.gson.Gson;
import me.mouamle.sync.packet.Packet;
import org.java_websocket.WebSocket;

public abstract class Handler<P, R> {

    protected final Gson gson = new Gson();

    public abstract void handle(WebSocket socket, P payload);

    public abstract String getPacketName();

    public abstract Class<P> getPayloadType();

    public abstract Class<R> getResponseType();

    protected void reply(WebSocket socket, Object response) {
        socket.send(gson.toJson(new Packet(getPacketName(), response)));
    }

}

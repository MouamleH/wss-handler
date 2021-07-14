package me.mouamle.sync.packet.handler;

import com.google.gson.Gson;
import me.mouamle.sync.packet.Packet;
import org.java_websocket.WebSocket;

/**
 * The Handler for a specific packet type
 *
 * @param <P> The payload type
 * @param <R> The reply type
 */
public abstract class Handler<P, R> {

    protected final Gson gson = new Gson();

    public abstract void handle(WebSocket socket, P payload);

    public abstract String getPacketType();

    public abstract Class<P> getPayloadType();

    public abstract Class<R> getResponseType();

    protected void reply(WebSocket socket, R response) {
        socket.send(gson.toJson(new Packet(getPacketType(), response)));
    }

}

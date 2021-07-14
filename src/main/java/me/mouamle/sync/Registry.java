package me.mouamle.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.mouamle.sync.packet.handler.Handler;

import java.util.*;

@SuppressWarnings("rawtypes")
public class Registry {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    private static final Map<String, Class> payloadTypes = new HashMap<>();
    private static final Map<Class, Handler> packetHandlers = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static String exportJson() {
        List<Object> packets = new ArrayList<>();
        for (Handler handler : packetHandlers.values()) {
            final Map<String, Object> packet = new HashMap<>();
            packet.put("type", handler.getPacketType());
            packet.put("request_payload", gson.fromJson("{}", handler.getPayloadType()));
            packet.put("response_payload", gson.fromJson("{}", handler.getResponseType()));
            packets.add(packet);
        }
        final Map<String, Object> json = new HashMap<>();
        json.put("packets", packets);
        return gson.toJson(json);
    }

    /**
     * Registers the handler and its payload type
     */
    public static void register(Handler handler) {
        payloadTypes.put(handler.getPacketType(), handler.getPayloadType());
        packetHandlers.put(handler.getPayloadType(), handler);
    }

    static Optional<Class> getPayloadClass(String packetType) {
        return Optional.ofNullable(payloadTypes.get(packetType));
    }

    static Optional<Handler> getHandler(Class payloadType) {
        return Optional.ofNullable(packetHandlers.get(payloadType));
    }

}

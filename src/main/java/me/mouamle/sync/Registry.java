package me.mouamle.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.mouamle.sync.packet.handler.Handler;

import java.util.*;

@SuppressWarnings("rawtypes")
public class Registry {

    private static final Map<String, Class> payloadTypes = new HashMap<>();
    private static final Map<Class, Handler> packetHandlers = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    @SuppressWarnings("unchecked")
    public static String exportJson() {
        List<Object> packets = new ArrayList<>();
        for (Handler handler : packetHandlers.values()) {
            final Map<String, Object> packet = new HashMap<>();
            packet.put("type", handler.getPacketName());
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
        registerPayload(handler.getPacketName(), handler.getPayloadType());
        registerHandler(handler);
    }

    /**
     * Registers a handler with a custom name, ignores {@link Handler#getPacketName()}
     */
    public static void register(String packetName, Handler handler) {
        registerPayload(packetName, handler.getPayloadType());
        registerHandler(handler);
    }

    private static void registerPayload(String packetType, Class payloadClass) {
        payloadTypes.put(packetType, payloadClass);
    }

    private static void registerHandler(Handler handler) {
        packetHandlers.put(handler.getPayloadType(), handler);
    }

    static Optional<Class> getPayloadClass(String packetType) {
        return Optional.ofNullable(payloadTypes.get(packetType));
    }

    static Optional<Handler> getHandler(Class payloadType) {
        return Optional.ofNullable(packetHandlers.get(payloadType));
    }

}

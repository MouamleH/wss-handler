package me.mouamle.sync.packet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.mouamle.sync.packet.handler.Handler;

import java.util.*;

@SuppressWarnings("rawtypes")
public class Registry {

    private static final Map<String, Class> payloadTypes = new HashMap<>();
    private static final Map<Class, Handler> packetHandlers = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    /**
     * @return s
     */
    @SuppressWarnings("unchecked")
    public static String exportJson() {
        List<Object> payloads = new ArrayList<>();
        payloadTypes.forEach((type, clazz) -> {
            final Map<String, Object> payload = new HashMap<>();
            payload.put("type", type);
            payload.put("payload", gson.fromJson("{}", clazz));
            payloads.add(payload);
        });
        final Map<String, Object> json = new HashMap<>();
        json.put("packets", payloads);
        return gson.toJson(json);
    }


    public static void register(Handler handler) {
        registerPayload(handler.getPacketName(), handler.getPayloadType());
        registerHandler(handler);
    }


    public static void register(String packetType, Handler handler) {
        registerPayload(packetType, handler.getPayloadType());
        registerHandler(handler);
    }

    public static void registerPayload(String packetType, Class payloadClass) {
        payloadTypes.put(packetType, payloadClass);
    }

    public static void registerHandler(Handler handler) {
        packetHandlers.put(handler.getPayloadType(), handler);
    }

    public static Optional<Class> getPayloadClass(String packetType) {
        return Optional.ofNullable(payloadTypes.get(packetType));
    }

    public static Optional<Handler> getHandler(Class payloadType) {
        return Optional.ofNullable(packetHandlers.get(payloadType));
    }

}
